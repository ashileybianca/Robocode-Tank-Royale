import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

import java.util.Random;

public class AshBot extends Bot {

    public static void main(String[] args) {
        new AshBot().start();
    }

    // faz load do arquivo de configuração do robô
    AshBot() {
        super(BotInfo.fromFile("AshBot.json"));
    }

    // Called when a new round is started -> initialize and do some movement
    @Override
    public void run() {

        Random random = new Random();

        // adicionado cores ao robô
        setBodyColor(Color.FUCHSIA);
        setTurretColor(Color.WHITE);
        setRadarColor(Color.PURPLE);
        setBulletColor(Color.WHITE);
        setScanColor(Color.WHITE);

        while (isRunning()) { 
            double[] angulos = { -15, -10, -5, 5, 10, 15, 20, 25, 30, 35, 40, 45 };
            double angulo = angulos[random.nextInt(angulos.length)];
            turnLeft(angulo);
            forward(20);
            adjustGunAndRadar();
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {

        if(getEnergy() < 30){
            turnRight(90);
            back(100);
            forward(20);
        } else{

            double direcao = gunBearingTo(e.getBullet().getX(), e.getBullet().getY());
            turnLeft(direcao);
            back(100);
        }
        
    }


    public void onScannedBot(ScannedBotEvent e) {

        var direcaoArma = gunBearingTo(e.getX(), e.getY());
        turnGunLeft(direcaoArma);

        double distance = Math.sqrt(Math.pow(e.getX() - getX(), 2) + Math.pow(e.getY() - getY(), 2));

        int power = 1;

        if (distance < 200 && getEnergy() > 30) {
            power = 3;
        } else if (distance < 300 && getEnergy() > 30){
            power = 2;
        } else {
            power = 1;
        }

        smartFire(power);
    }

    private void smartFire(int power) {
        fire(power);
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        double distance = bearingTo(e.getX(), e.getY());

        if (distance > -10 && distance < 10) {
            fire(3);
        }

        if (e.isRammed()) {
            turnRight(10);
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        back(100); 
        turnRight(90);
    }

    @Override
    public void onDeath(DeathEvent e) {

    }
    
    private void adjustGunAndRadar() {
        turnGunRight(getTurnRemaining()); 
        turnRadarRight(getTurnRemaining());
    }
}
