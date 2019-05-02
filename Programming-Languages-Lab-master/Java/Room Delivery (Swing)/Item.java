public class Item{
	public String name;
	public Double price;
	public Integer quantity;
	public Boolean tbp;

	public Item(String name, Double price, Integer quantity, Boolean tbp){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.tbp = tbp;
	}

	// public JSONObject toJSON() {
	// 	JSONObject jo = new JSONObject();
	// 	jo.put("name",name);
	// 	jo.put("price",price);
	// 	jo.put("quantity",quantity);
	// 	return jo;
	// }
}