package com.esssolutions.flappybird.level;

import com.esssolutions.flappybird.graphics.Shader;
import com.esssolutions.flappybird.graphics.Texture;
import com.esssolutions.flappybird.graphics.VertexArray;
import com.esssolutions.flappybird.input.Input;
import com.esssolutions.flappybird.math.Matrix4f;
import com.esssolutions.flappybird.math.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Bird {

	private float SIZE = 1.0f;
	private VertexArray mesh;
	private Texture texture;

	private Vector3f position = new Vector3f();
	private float rot;
	private float delta = 0.0f;

	public Bird() {
		float[] vertices = new float[]{
				-SIZE / 2.0f,  -SIZE / 2.0f, 0.2f,
				-SIZE / 2.0f,   SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f,   SIZE / 2.0f, 0.2f,
				 SIZE / 2.0f,  -SIZE / 2.0f, 0.2f
		};

		byte[] indices = new byte[]{
				0, 1, 2,
				2, 3, 0
		};

		float[] tcs = new float[]{
				0, 1,
				0, 0,
				1, 0,
				1, 1
		};

		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("res/bird.png");
	}

	public void update(){
		position.y -= delta;
		if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE)){
			delta = -0.15f;
		}
		else
			delta += 0.01f;

		rot = -delta * 90.0f;
	}

	public void fall(){
		delta = -0.15f;
	}

	public void render(){
		Shader.BIRD.enable();
		Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
		texture.bind();
		mesh.render();
		Shader.BIRD.disable();
	}

	public float getY() {
		return position.y;
	}

	public float getSize() {
		return SIZE;
	}
}
