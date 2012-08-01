package dataStructures;

public class ModelCheckerOption extends NameKeyObject<ModelChecker>{
	
	// identifiers used to represent different engines
	public static final int MODEL_CHECKER_0 = 0;
	public static final int MODEL_CHECKER_1 = 1;
	public static final String[] modelCheckerNames = { "CADENCE SMV", "NuSMV" };
	
	public static ModelCheckerOption[] getAllOptions(){
		ModelCheckerOption[] allOptions = new ModelCheckerOption[modelCheckerNames.length];
		for(int i=0;i<allOptions.length;i++){
			allOptions[i]=new ModelCheckerOption(i);
		}
		return allOptions;
	}

	public ModelCheckerOption(Integer key) {
		super(modelCheckerNames[key], key, null);
	}
	
	@Override
	public String toString(){
		return getName();
	}
	
	@Override
	public ModelChecker getObject(){
		if(this.key==MODEL_CHECKER_0){
			;//get reference to static model checker 0
		}else if(this.key==MODEL_CHECKER_1){
			;//get reference to static model checker 1
		}
		return null;
	}

	public static ModelCheckerOption getOption(
			String selectedModelChecker) {
		if(selectedModelChecker==null){
			System.err.println("selectedModelChecker string is null");
			return null;
		}
		for(int i=0;i<modelCheckerNames.length;i++){
			System.out.println(selectedModelChecker +" " +modelCheckerNames[i]);
			if(selectedModelChecker.equals(modelCheckerNames[i])){
				return new ModelCheckerOption(i);
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ModelCheckerOption)) {
			return false;
		} else if (!this.name.equals( ((ModelCheckerOption)obj).getName())) {
			return false;
		}
		return true;
		
	}

}
