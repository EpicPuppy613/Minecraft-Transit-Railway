package org.mtr.mod.render.pids;

import org.mtr.core.tool.Utilities;
import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.ArrayList;

public class LineNameModule extends TextModule {
    public final String type = "lineName";

    public LineNameModule(float x, float y, float width, float height, ReaderBase data) {
        super(x, y, width, height, data);
    }

    @Override
    protected ArrayList<String> getText(ObjectList<ArrivalResponse> arrivals) {
        ArrayList<String> text = new ArrayList<>();
        ArrivalResponse arrivalResponse = Utilities.getElement(arrivals, arrival);
        if (arrivalResponse == null) {
            return null;
        }
        text.add(arrivalResponse.getRouteName());
        return text;
    }
}
