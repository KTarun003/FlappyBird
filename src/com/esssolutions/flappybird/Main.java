package com.esssolutions.flappybird;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.esssolutions.flappybird.graphics.Shader;
import com.esssolutions.flappybird.input.Input;
import com.esssolutions.flappybird.level.Level;
import com.esssolutions.flappybird.math.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;


public class Main implements Runnable{

	private int width = 1280;
	private int height = 720;
	private long window;
	private boolean running = false;

	private Thread thread;

	private Level level;

	public void start(){
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public static void main(String[] args) {
		new Main().start();
	}

	private void init() {
		if(!glfwInit()){
			glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			return;
		}

		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flappy Bird", NULL, NULL);
		if(window == NULL){
			return;
		}
		GLFWVidMode vidmode = glfwGetVideoMode((glfwGetPrimaryMonitor()));
		glfwSetWindowPos(window, (vidmode.width() - width)/2, (vidmode.height() - height)/2);

		glfwSetKeyCallback(window, new Input());

		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glfwShowWindow(window);

		System.out.println("OpenGL : " + glGetString(GL_VERSION));

		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		Shader.loadAll();


		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f,10.0f,-10.f*9.0f /16.0f,10.f*9.0f /16.0f,-1.0f,1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.setUniform1i("tex",1);

		Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex",1);

		Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PIPE.setUniform1i("tex",1);

		level = new Level();
	}

	public void run() {
		init();

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0/60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1.0){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer +=1000;
				System.out.println(updates + " ups " + frames + " fps");
				frames = 0;
				updates = 0;
			}
			if(glfwWindowShouldClose(window))
				running = false;
		}

		glfwDestroyWindow(window);
		glfwTerminate();
	}

	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		int error = glGetError();
		if(error != GL_NO_ERROR){
			System.out.println(error);
		}
		glfwSwapBuffers(window);
	}

	private void update(){
		glfwPollEvents();
		level.update();
	}

}
