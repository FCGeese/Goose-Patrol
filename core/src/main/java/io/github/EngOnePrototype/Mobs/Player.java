    package io.github.EngOnePrototype.Mobs;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.math.Vector2;

    public class Player extends Entity {
        int defaultSpeed;

        public Player(int x, int y, int defaultSpeed) {
            super(x, y, 0, 0, 10);
            this.defaultSpeed = defaultSpeed;
        }

        @Override 
        public void update(){
            //key watchers
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                speed = 2*speed;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                if(Gdx.input.isKeyPressed(Input.Keys.A)^(Gdx.input.isKeyPressed(Input.Keys.D))){
                    //stops the '45 degree rush'
                    speed =(int) Math.pow(speed, 2);
                    speed = speed/2;
                    speed = (int) Math.sqrt(speed);
                }
                y=y+speed;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                if(Gdx.input.isKeyPressed(Input.Keys.A)^(Gdx.input.isKeyPressed(Input.Keys.D))){
                    speed =(int) Math.pow(speed, 2);
                    speed = speed/2;
                    speed = (int) Math.sqrt(speed);
                }
                y=y-speed;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                x=x-speed;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)){
                x=x+speed;
            }
            speed= defaultSpeed;
        }
    
        @Override 
        public void draw(ShapeRenderer shape) {
            direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            shape.circle(x, y, size);
            Vector2 position = new Vector2(x,y);
            Vector2 reticle = new Vector2(
                x+(int)(Math.cos(Math.toRadians(direction)) * 50),
                y+(int)(Math.sin(Math.toRadians(direction)) * 50));
            shape.line(position, reticle);
        }
    }



    /*OLD MOUSE CHASING MOVEMENT
        public void update() {
            direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            // key 
            //strafe
            char strafeDirection=' ';
            if (Gdx.input.isKeyPressed(29) ^ Gdx.input.isKeyPressed(32)){
                if(Gdx.input.isKeyPressed(29)){
                    strafeDirection = 'l';
                }
                else{
                    strafeDirection = 'r';
                }
                speed = Settings.playerSpeed;
                super.update(strafeDirection);
                speed = 0;        
            }

            //forward/reverse
            if (Gdx.input.isKeyPressed(51)) {
                if (Gdx.input.isKeyPressed(59)) {
                    speed = Settings.playerSprintSpeed;
                }
                else{
                    speed = Settings.playerSpeed;
                }
            } else {
                if (Gdx.input.isKeyPressed(47)) {
                    speed = -Settings.playerSpeed;
                } else {
                    speed = 0;
                }

            }
            
            super.update();
        }
        */