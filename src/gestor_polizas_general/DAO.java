
package gestor_polizas_general;

import java.util.List;

public abstract interface DAO {
        //metodos CRUD
    int create(Object obj);
    List read();
    int update(Object lista);
    int delate(Object obj);
}
