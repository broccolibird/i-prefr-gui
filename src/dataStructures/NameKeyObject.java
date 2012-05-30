package dataStructures;

class NameKeyObject<A>{

	protected String name;
	protected Integer key;
	protected A object;

	public NameKeyObject(String name, Integer key, A object) {
		this.name = name;
		this.key = key;
		this.object=object;
	}

	public String getName() {
		return name;
	}

	public Integer getKey() {
		return key;
	}
	
	public A getObject(){
		return object;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKey(Integer key) {
		this.key = key;
		try{
			throw new Exception();	
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void setObject(A object) {
		this.object = object;
	}

}
