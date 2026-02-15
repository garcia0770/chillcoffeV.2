    package com.ChillCoffeV1_4.servicio;

    import com.ChillCoffeV1_4.modelo.Orden;
    import com.ChillCoffeV1_4.modelo.OrdenHistorial;
    import com.ChillCoffeV1_4.repositorio.OrdenHistorialRepository;
    import com.ChillCoffeV1_4.repositorio.OrdenRepository;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class OrdenService {

        private final OrdenRepository ordenRepo;
        private final OrdenHistorialRepository historialRepo;

        public OrdenService(OrdenRepository ordenRepo, OrdenHistorialRepository historialRepo) {
            this.ordenRepo = ordenRepo;
            this.historialRepo = historialRepo;
        }

        public List<Orden> listarActivas() {
            return ordenRepo.findByActivoTrueOrderByCreadoEnDesc();
        }

        public Orden crear(String nombre) {
            Orden o = new Orden();
            o.setNombre(nombre);
            o.setCreadoEn(LocalDateTime.now());
            o.setActivo(true);
            Orden saved = ordenRepo.save(o);

            OrdenHistorial h = new OrdenHistorial();
            h.setOrdenOriginalId(saved.getId());
            h.setNombre(saved.getNombre());
            h.setCreadoEn(saved.getCreadoEn());
            h.setProcesadoEn(null);
            h.setAccion("CREADA"); // usa 'accion' como en tu entidad
            historialRepo.save(h);

            return saved;
        }

        @Transactional
        public Optional<Orden> atenderYArchivar(Long id) {
            Optional<Orden> opt = ordenRepo.findById(id);
            if (opt.isPresent()) {
                Orden ord = opt.get();
                ord.setActivo(false);
                ordenRepo.save(ord);

                OrdenHistorial h = new OrdenHistorial();
                h.setOrdenOriginalId(ord.getId());
                h.setNombre(ord.getNombre());
                h.setCreadoEn(ord.getCreadoEn());
                h.setProcesadoEn(LocalDateTime.now());
                h.setAccion("ATENDIDA");
                historialRepo.save(h);

                return Optional.of(ord);
            }
            return Optional.empty();
        }

        @Transactional
        public boolean eliminarYRegistrar(Long id) {
            Optional<Orden> opt = ordenRepo.findById(id);
            if (opt.isPresent()) {
                Orden ord = opt.get();

                OrdenHistorial h = new OrdenHistorial();
                h.setOrdenOriginalId(ord.getId());
                h.setNombre(ord.getNombre());
                h.setCreadoEn(ord.getCreadoEn());
                h.setProcesadoEn(LocalDateTime.now());
                h.setAccion("ELIMINADA");
                historialRepo.save(h);

                ord.setActivo(false);
                ordenRepo.save(ord);

                return true;
            }
            return false;
        }

        public List<OrdenHistorial> historialCompleto() {
            return historialRepo.findAll();
        }
    }