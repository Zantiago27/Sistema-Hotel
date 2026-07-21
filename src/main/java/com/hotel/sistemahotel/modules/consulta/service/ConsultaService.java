package com.hotel.sistemahotel.modules.consulta.service;

import com.hotel.sistemahotel.modules.consulta.dto.DniResponseDto;
import com.hotel.sistemahotel.modules.consulta.dto.RucResponseDto;
import com.hotel.sistemahotel.shared.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class ConsultaService {

    @Value("${apis.netpe.token}")
    private String token;

    @Value("${apis.netpe.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public DniResponseDto consultarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw BusinessException.badRequest("El DNI debe tener exactamente 8 dígitos");
        }

        String url = baseUrl + "/v1/reniec/dni?numero=" + dni;

        try {
            HttpHeaders headers = buildHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();

            if (body == null) {
                throw BusinessException.notFound("No se encontró información para el DNI: " + dni);
            }

            // Campos según documentación Decolecta
            String nombres        = (String) body.get("first_name");
            String apellidoPat    = (String) body.get("first_last_name");
            String apellidoMat    = (String) body.get("second_last_name");
            String nombreCompleto = (String) body.get("full_name");

            return DniResponseDto.builder()
                    .dni(dni)
                    .nombres(nombres)
                    .apellidoPaterno(apellidoPat)
                    .apellidoMaterno(apellidoMat)
                    .nombreCompleto(nombreCompleto)
                    .build();

        } catch (HttpClientErrorException e) {
            log.error("Error consultando DNI {}: {} - {}", dni, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw BusinessException.notFound("No se encontró el DNI: " + dni);
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw BusinessException.badRequest("Token de API inválido o expirado");
            }
            throw BusinessException.badRequest("Error al consultar el DNI");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado consultando DNI {}: {}", dni, e.getMessage());
            throw BusinessException.badRequest("Error al conectar con el servicio RENIEC");
        }
    }

    public RucResponseDto consultarRuc(String ruc) {
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw BusinessException.badRequest("El RUC debe tener exactamente 11 dígitos");
        }

        String url = baseUrl + "/v1/sunat/ruc?numero=" + ruc;

        try {
            HttpHeaders headers = buildHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();

            if (body == null) {
                throw BusinessException.notFound("No se encontró información para el RUC: " + ruc);
            }

            // Campos según documentación Decolecta
            return RucResponseDto.builder()
                    .ruc(ruc)
                    .razonSocial((String) body.get("razon_social"))
                    .estado((String) body.get("estado"))
                    .condicion((String) body.get("condicion"))
                    .direccion((String) body.get("direccion"))
                    .ubigeo((String) body.get("ubigeo"))
                    .distrito((String) body.get("distrito"))
                    .provincia((String) body.get("provincia"))
                    .departamento((String) body.get("departamento"))
                    .build();

        } catch (HttpClientErrorException e) {
            log.error("Error consultando RUC {}: {} - {}", ruc, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                throw BusinessException.badRequest("RUC inválido: " + ruc);
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw BusinessException.badRequest("Token de API inválido o expirado");
            }
            throw BusinessException.badRequest("Error al consultar el RUC");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado consultando RUC {}: {}", ruc, e.getMessage());
            throw BusinessException.badRequest("Error al conectar con el servicio SUNAT");
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}