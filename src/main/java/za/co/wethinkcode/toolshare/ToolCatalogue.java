package za.co.wethinkcode.toolshare;

import java.util.List;
import java.util.Optional;

public interface ToolCatalogue {

    List<Tool> findAll();

    Optional<Tool> findById(long id);
}
