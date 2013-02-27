package pt.gulbenkian.igc.nmd;

public interface SimulationDialog {
	
	public byte[] getInitialUnitaryValues();
	public byte[] getEnvironmentalValues();
	public SimulationType getSimulationType();

}
