package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.model.EnderecoWeb;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsultaCEPService {

    public EnderecoWeb getEnderecoWeb(String cep) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<EnderecoWeb> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EnderecoWeb.class);
            EnderecoWeb enderecoWebs = responseEntity.getBody();
            if(enderecoWebs.getErro() != null && enderecoWebs.getErro()) throw new ServiceException("mensagemErroCepNaoEncontrado");
            return enderecoWebs;
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().equals("mensagemErroCepNaoEncontrado")) throw new ServiceException(e.getMessage());
            throw new ServiceException("mensagemErroBuscaCep");
        }
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 20000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

}
