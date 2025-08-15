package com.faraz.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    int x;
    int y;
    int size;
    int xSpeed;
    int ySpeed;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        //todo delete
        Model m;
        ModelData md;
        ModelBuilder modelBuilder = new ModelBuilder();
        ModelInstance mi;

        //todo delete

        x += xSpeed;
        y += ySpeed;
        if (x < size || (x > Gdx.graphics.getWidth() - size)) {
            xSpeed = -xSpeed;
        }
        if (y < size || (y > Gdx.graphics.getHeight() - size)) {
            ySpeed = -ySpeed;
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.circle(x, y, size);
    }
}
