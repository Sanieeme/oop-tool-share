package za.co.wethinkcode.toolshare;

import java.util.List;
import java.util.Optional;

public class InMemoryToolCatalogue implements ToolCatalogue {

    private final List<Tool> tools;

    public InMemoryToolCatalogue(List<Tool> tools) {
        this.tools = List.copyOf(tools);
    }

    public static InMemoryToolCatalogue seeded() {
        return new InMemoryToolCatalogue(List.of(
                new HandTool(1, "Claw Hammer"),
                new HandTool(2, "Step Ladder"),
                new GardenTool(3, "Spade"),
                new GardenTool(4, "Wheelbarrow")));
    }

    @Override
    public List<Tool> findAll() {
        return tools;
    }

    @Override
    public Optional<Tool> findById(long id) {
        return tools.stream().filter(t -> t.getId() == id).findFirst();
    }
}
