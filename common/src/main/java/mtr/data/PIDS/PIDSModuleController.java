package mtr.data.PIDS;

import java.util.ArrayList;

public class PIDSModuleController {
    private ArrayList<PIDSModule> modules = new ArrayList<>();

    public PIDSModuleController addModule(PIDSModule module) {
        modules.add(module);
        return this;
    }

    public int getLength() {
        return modules.size();
    }

    public PIDSModule getModule(int index) {
        return modules.get(index);
    }
}
