package com.hotel.sistemahotel.config;

import com.hotel.sistemahotel.modules.reserva.entity.Reserva;
import com.hotel.sistemahotel.modules.reserva.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

//esta clase es para generar alertas

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class AlertaScheduler {

    private final ReservaRepository reservaRepository;

    @Scheduled(fixedDelay = 60000) // cada 60 segundos
    @Transactional
    public void marcarReservasVencidas() {
        List<Reserva> vencidas = reservaRepository
                .findTodasReservasVencidas(OffsetDateTime.now());

        if (!vencidas.isEmpty()) {
            log.warn("Reservas vencidas encontradas: {}", vencidas.size());
            vencidas.forEach(r -> {
                r.setEstado("VENCIDA");
                reservaRepository.save(r);
                log.warn("Reserva vencida: {} - cliente: {}", r.getId(), r.getClienteId());
            });
        }
    }
}