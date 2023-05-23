package mtr.data.PIDS;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Route;
import mtr.data.ScheduleEntry;
import mtr.render.MatrixStackHolder;

public abstract class PIDSModule {

    //Module Size
    public final int left;
    public final int top;
    public final int width;
    public final int height;
    public final int arrival;

    public PIDSModule(int left, int top, int width, int height, int arrival) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.arrival = arrival;
    }

    //TODO: ArrivalTimeModule
    //TODO: PlatformNumberModule
    //TODO: DestinationModule
    //TODO: TextModule
    //TODO: StopsAtModule
    //TODO: StopsAtPageModule
    //TODO: TrainLengthModule
    //TODO: GameTimeModule
    //TODO: WorldTimeModule
    //TODO: LineNameModule
    //TODO: ArrivalNumberModule
    public void render(IPIDS.TileEntityPIDS entity, MatrixStackHolder matrixStackHolder, PoseStack matricies, Route route, ScheduleEntry schedule) {}

}
