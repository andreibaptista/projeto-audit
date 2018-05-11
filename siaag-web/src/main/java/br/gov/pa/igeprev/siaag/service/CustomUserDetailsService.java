package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioService usuarioService;


    @Override
    public UserDetails loadUserByUsername(String login) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Usuario usuarioAtivo = usuarioService.findByLoginAndAtivo(StringUtils.removeSpecialCharacter(login), true);
            if (usuarioAtivo != null && usuarioAtivo.getId() != null) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuarioAtivo.getPerfil().getDescricao());
                if (usuarioAtivo.getPrimeiroAcesso()) usuarioAtivo.setSenha(encoder.encode(""));
                UserDetails userDetails = new User(usuarioAtivo.getLogin(), usuarioAtivo.getSenha(),
                        Collections.singletonList(authority));
                return userDetails;
            } else {
                throw new UsernameNotFoundException("Usuário não encontrado.");
            }
            // TODO: Tratar as exceções.
        } catch (ServiceException e) {
            return null;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        } catch (Exception e) {
            return null;
        }
    }
}
