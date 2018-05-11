/*
 *  JSFUniversalConverter
 *  
 *  1.0.0
 *  
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils.converter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 * 
 * Converter resposável para ser aplicado em qualquer componente JSF
 * que exija um conversor, sem considerar o tipo de class
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 08/03/2017
 */
@SuppressWarnings("unchecked")
@FacesConverter("JSFUniversalConverter")
public class JSFUniversalConverter implements Converter {
   

	@SuppressWarnings("rawtypes")
	public Object getAsObject(FacesContext facesContext, UIComponent uicomp, String value) {
	        if (value == null || value.trim().isEmpty())
	            return null;
	        List<Object> items = new ArrayList<Object>();

	        Object item = null;
	        if(!uicomp.getChildren().isEmpty()){
	        List<UIComponent> uicompList = uicomp.getChildren();
		        for (UIComponent comp : uicompList){
		            if (comp instanceof UISelectItems){
		            	if(((UISelectItems) comp).getValue()!=null){
		            		items.addAll((List<Object>) ((UISelectItems) comp).getValue());
		            	}
		            }
		        }
	        }else{
				if (uicomp instanceof PickList) {
					Object dualList = ((PickList) uicomp).getValue();
					DualListModel dl = (DualListModel) dualList;
					for (Iterator iterator = dl.getSource().iterator(); iterator.hasNext();) {
						Object o = iterator.next();
						String id = o.toString();
						if (value.equals(id)) {
							item = o;
							items.add(item);
							break;
						}
					}
					if (item == null) {
						for (Iterator iterator1 = dl.getTarget().iterator(); iterator1.hasNext();) {
							Object o = iterator1.next();
							String id = o.toString();
							if (value.equals(id)) {
								item = o;
								items.add(item);
								break;
							}
						}
					}
				}else if(uicomp instanceof AutoComplete){
					Object autoComplete = ((AutoComplete) uicomp).getSubmittedValue();
					if (value.equals(autoComplete)) {
						items.add(autoComplete);
					}
				}
	        }
	        
	        for (Object each : items){
	        	if(each instanceof SelectItemGroup){
	        		SelectItemGroup e = (SelectItemGroup) each;
	        		for(SelectItem i : e.getSelectItems()){
	        			if(i.getLabel().equals(value) || i.getValue().toString().equals(value)){
	        				 item = i.getValue();
	        				 break;
	        			}
	        		}
	        	}
	        	
	            if (each.toString().equals(value)){
	                item = each;
	            }
	        }
	        return item;
	    }

	    public String getAsString(FacesContext facesContext, UIComponent uicomp, Object value) {
	        if (value == null || value == "")
	            return "";
	        return value.toString();

	    }

}