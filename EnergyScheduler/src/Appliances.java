import java.util.Date;

public class Appliances {

	public String ApplianceID;
	public String ApplianceName;
	public boolean CompletionFlag = false;
	public Date StartTime = new Date(2001, 01, 01);
	public Date EndTime = new Date(2001, 01, 01);
	public int Priority;
	public int PowerRequirement;
	public int TimeToCompleteJob;
	public boolean DependanceFlag = false;
	public String DependentAppliance;	
	public Date CurrentDate = new Date();
	public boolean UserOverrideFlag = false;
	
}
