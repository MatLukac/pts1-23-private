package interfaces;

import records.GiveUsedTilesResult;
import records.TakeUsedTilesResult;

public interface UsedTilesInterface {
    TakeUsedTilesResult take();

    GiveUsedTilesResult give();

    String state();
}
