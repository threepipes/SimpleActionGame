package scene;

import java.util.HashMap;

public class SceneController {
	private Scene nowScene;
	private HashMap<String, Scene> scenes = new HashMap<String, Scene>();
	public SceneController() {
		createScene();
	}
	
	private void createScene(){
		
	}
	
	public Scene getScene(){
		return nowScene;
	}
}
