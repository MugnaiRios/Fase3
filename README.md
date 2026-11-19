# Gestor de Pólizas

Proyecto final de Programación Orientada a Objetos. Sistema de escritorio para un despacho de seguros que registra clientes, vehículos, pólizas, coberturas y pagos.

Hecho con Java (Swing), MySQL y NetBeans.

## Equipo

- López Gómez Enrique
- García Hernández Ana Belinda
- Mugnai Ríos Ana

## Estructura

| Carpeta | Contenido |
|---|---|
| `src/gestor_polizas` | `Inicio`: arranca la aplicación |
| `src/gestor_polizas_general` | `Conexion`, `DAO`, `Bienvenida` (ventana principal) y `Controlador` |
| `src/gestor_polizas_usuario` | CRUD de usuarios: `Usuario`, `UsuarioDAO`, `UsuarioVista`, `UsuarioControl` |
| `bd/gestor_polizas.sql` | Script que crea la base de datos con sus 10 tablas y datos de prueba |
| `lib` | Conector JDBC de MySQL |
| `docs/evidencias` | Capturas de la base de datos funcionando |

## Cómo ejecutarlo

1. Abrir MySQL (o MariaDB/XAMPP) y ejecutar `bd/gestor_polizas.sql`.
2. Abrir el proyecto en NetBeans.
3. El conector de MySQL ya viene en `lib/mysql-connector-j-8.0.33.jar` y el proyecto lo usa desde ahí.
4. Si su usuario o contraseña de MySQL no es `root` sin contraseña, cambiarlo en `src/gestor_polizas_general/Conexion.java`.
5. Ejecutar con Run. En el menú Opciones > Usuarios se abre el CRUD de usuarios.
