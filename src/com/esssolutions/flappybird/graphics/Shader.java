package com.esssolutions.flappybird.graphics;

import static org.lwjgl.opengl.GL20.*;

import com.esssolutions.flappybird.math.Matrix4f;
import com.esssolutions.flappybird.math.Vector3f;
import com.esssolutions.flappybird.util.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

public class Shader {

	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	private boolean enabled = false;

	private int Id;
	private Map<String,Integer> locationCache = new HashMap<String, Integer>();

	public static Shader BG,BIRD,PIPE;

	private Shader(String vertex, String fragment){
		Id = ShaderUtils.load(vertex, fragment);
	}

	public static void loadAll(){
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
		PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
	}

	public int getUniform(String name){
		if(locationCache.containsKey(name))
			return locationCache.get(name);
		int result = glGetUniformLocation(Id, name);
		if(result == -1){
			System.err.println("Could not find uniform variable '" + name +"' !");
		}
		else
			locationCache.put(name, result);
		return result;
	}

	public void setUniform3f(String name, Vector3f vector){
		if(!enabled)
			enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}

	public void setUniform1i(String name, int value){
		if(!enabled)
			enable();
		glUniform1i(getUniform(name), value);
	}

	public void setUniform1f(String name, float value){
		if(!enabled)
			enable();
		glUniform1f(getUniform(name), value);
	}

	public void setUniform2f(String name, float x, float y){
		if(!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
	}

	public void setUniformMat4f(String name, Matrix4f matrix){
		if(!enabled)
			enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

	public void enable(){
		glUseProgram(Id);
		enabled = true;
	}

	public void disable(){
		glUseProgram(0);
		enabled = false;
	}
}