package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class Vision {
    Limelight3A limelight;
    public Vision(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start();
        limelight.pipelineSwitch(0);
    }
    public LLResult getResult(){
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return null;
        }
        return result;
    }
    public int getLatestId(){
        LLResultTypes.FiducialResult latest = getLatest();
        if (latest != null) {
            return latest.getFiducialId();
        }
        return -1;
    }
    public LLResultTypes.FiducialResult getLatest(){
        List<LLResultTypes.FiducialResult> latests = getLatests();
        if (latests != null && !latests.isEmpty()) {
            return latests.get(0);
        }
        return null;
    }
    public List<LLResultTypes.FiducialResult> getLatests(){
        LLResult result = getResult();
        if (result == null) {
            return null;
        }
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials != null && !fiducials.isEmpty()) {
            return fiducials;
        }
        return null;
    }
    public double getLatestTxDegreees(){
        LLResultTypes.FiducialResult latest = getLatest();
        if (latest != null) {
            return latest.getTargetXDegrees();
        }
        return -1;
    }
    public double getLatestTyDegreees(){
        LLResultTypes.FiducialResult latest = getLatest();
        if (latest != null) {
            return latest.getTargetYDegrees();
        }
        return -1;
    }
    public double getLatestTa(){
        LLResultTypes.FiducialResult latest = getLatest();
        if (latest != null) {
            return latest.getTargetArea();
        }
        return -1;
    }
}
