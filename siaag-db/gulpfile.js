//MUDAR PARA A VERSAO QUE VOCE DESEJA
const version='SIAAG-1.0-0007'
const author="aleixo.porpino"



const gulp = require("gulp");
const fs = require("fs");
const path = require('path');
const glob = require('glob');
const xmlEdit = require('gulp-edit-xml');
const through2 = require('through2');
const File = require('vinyl');
const concat = require('gulp-concat');
const rename = require("gulp-rename");



const basePath='db'
const sqlPath="sqls";
const changeSet=path.join("./",basePath,sqlPath,version,"/**/*.sql")
const changelog=path.join("./",basePath,"db.changelog.xml")// "./siaag-db/db/db.changelog.xml"

gulp.task('parse-comment',function(){
    return gulp.src(changeSet,{overwrite:false})
    .pipe(readComments())
    .pipe(gulp.dest(path.join(basePath,'dist',version)));
})

gulp.task('rm-comment',['parse-comment'],function(){
    return gulp.src(changeSet,{overwrite:false})
    .pipe(removeComments())
    //.pipe(concat('script.sql'))
    .pipe(gulp.dest(path.join(basePath,'dist',version)));
})


gulp.task('copy-change-set',function(){
    return gulp.src(changelog,{base:'.'})
    .pipe(rename("db.changelog.bak"))
    .pipe(gulp.dest(basePath));
})

gulp.task('change-set',['copy-change-set'], function () {    
    
    var changeLogFiles=[];
    var changeLogComments=[];

    let baseBath=path.parse(changelog).dir.replace(/\\/g,'/');
    let files=glob.sync(changeSet)
     //extrai comentários
    let re = new RegExp(/(\/\*([\w\W\'\s\r\n\*]*)\*\/)$/, 'm');

    files.forEach(function(element) {
        try{
            let file=fs.readFileSync(element).toString('utf8');
        
            let m = re.exec(file);
            element=element.replace(/\\/g,'/');
            
            element=element.replace(baseBath+"/",'');
            
            if(m && typeof m =='object' && m[2]){
                changeLogComments.push(m[2].trim());
            }
        }catch(e){
            console.log("nao foi possivel ler arquivo "+element);
        }

        changeLogFiles.push({
            $:{
                path:element,relativeToChangelogFile:"true" 
            }
        });
    }, this);

    gulp.src(changelog)
    .pipe(xmlEdit(convert,
        {
        parserOptions: {},
        builderOptions: {
            headless:false,
            renderOpts:{
                pretty: true
            },
            xmldec:{ 'version': '1.1', 'encoding': 'UTF-8', 'standalone': false }
        }
     }))
    .pipe(gulp.dest(basePath))

    
    function convert(xml){
    
        let found = xml.databaseChangeLog.changeSet.find(function(a,b){
            return a.$.id==version;
        });

        
        if(!found){
            let changeSetObj={
                $:{
                    id:version,
                    author:author
                },
                comment:changeLogComments,
                sqlFile:changeLogFiles
            }
            
            xml.databaseChangeLog.changeSet.push(changeSetObj);
        }else{
            console.log("change set "+version+" ja existe");
        }
     
        return xml;
    }

});


function readComments(){
    return through2.obj({ objectMode: true, allowHalfOpen: false },
        function(file, enc, next){
        let fileStr=file.contents.toString(enc);
        
        let re = new RegExp(/(\/\*([\w\W\'\s\r\n\*]*)\*\/)$/, 'm');
        let content;
        try{
            let m = re.exec(fileStr);
  
            if(m && typeof m =='object' && m[2]){
                content=m[2].trim()
            }
        }catch(e){

        }
        if(content){
            var base = path.join(file.path, '..');
            path.parse(file.path).name
            var fileItem = new File({
                base: base,
                path: path.join(base, path.parse(file.path).name+'.comment'),
                contents: new Buffer(content)
            });
            this.push(fileItem)
        }
        next();
    });
}

function removeComments(){
    return through2.obj({ objectMode: true, allowHalfOpen: false },
        function(file, enc, next){
        let fileStr=file.contents.toString(enc);
        
        let re = new RegExp(/(\/\*([\w\W\'\s\r\n\*]*)\*\/)$/, 'm');
        let content;
        try{
            let m = re.exec(fileStr);
  
            if(m && typeof m =='object' && m[2]){
                content=m[2].trim()

                fileStr=fileStr.replace(m[0],'')
            }
        }catch(e){

        }
        
        if(content){
            var base = path.join(file.path, '..');
            path.parse(file.path).name
            var fileItem = new File({
                base: base,
                path: path.join(base, path.parse(file.path).name+'.sql'),
                contents: new Buffer(fileStr+"\n/")
            });
            this.push(fileItem)
        }
        next();
    });
}
