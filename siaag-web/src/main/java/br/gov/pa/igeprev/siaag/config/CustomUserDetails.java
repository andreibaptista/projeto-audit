package br.gov.pa.igeprev.siaag.config;

import br.gov.pa.igeprev.siaag.model.Perfil;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
* Classe de custom que implementa UserDetails.
*
* @author  José Aleixo Araujo Porpino Filho
* @version 1.0
* @since   19/01/2018 
*/
public class CustomUserDetails extends Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	PerfilRepository perfilRepository;

	public CustomUserDetails(final Usuario usuario) {
        super(usuario);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<Perfil> perfis = new ArrayList<Perfil>();
		perfis.add(super.getPerfil());
		return perfis
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getDescricao()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return super.getSenha();
    }

    @Override
    public String getUsername() {
        return super.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
