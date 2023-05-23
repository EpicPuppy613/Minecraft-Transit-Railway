package mtr.data.PIDS;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Route;
import mtr.data.ScheduleEntry;
import mtr.render.MatrixStackHolder;

public class PIDSArrivalTimeModule extends PIDSModule {
    public PIDSArrivalTimeModule(int left, int top, int width, int fontSize, int arrival) {
        super(left, top, width, fontSize + 4, arrival);
    }

    @Override
    public void render(IPIDS.TileEntityPIDS entity, MatrixStackHolder matrixStackHolder, PoseStack matricies, Route route, ScheduleEntry schedule) {

    }
}
