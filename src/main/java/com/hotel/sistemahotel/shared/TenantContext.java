package com.hotel.sistemahotel.shared;

import java.util.UUID;

//esta clase guarda el id de la empresa, sucursla y usuario de manra global para usarlos en cualquier parte del codigo
public class TenantContext {

    private static final ThreadLocal<UUID> empresaId  = new ThreadLocal<>();
    private static final ThreadLocal<UUID> sucursalId = new ThreadLocal<>();
    private static final ThreadLocal<UUID> usuarioId  = new ThreadLocal<>();

    public static void setEmpresaId(UUID id)  { empresaId.set(id); }
    public static void setSucursalId(UUID id) { sucursalId.set(id); }
    public static void setUsuarioId(UUID id)  { usuarioId.set(id); }

    public static UUID getEmpresaId()  { return empresaId.get(); }
    public static UUID getSucursalId() { return sucursalId.get(); }
    public static UUID getUsuarioId()  { return usuarioId.get(); }

    public static void clear() {
        empresaId.remove();
        sucursalId.remove();
        usuarioId.remove();
    }
}