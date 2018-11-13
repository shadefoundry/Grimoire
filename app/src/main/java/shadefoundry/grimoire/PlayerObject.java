package shadefoundry.grimoire;

public class PlayerObject {
    int id;
    int life;
    int poison;
    int energy;
    int experience;
    int[] mana = new int[]{0,0,0,0,0,0};
    String log;

    public PlayerObject(){}

    public PlayerObject(int _id, int _life,int _poison, int _energy,int _experience,String _log){
        this.id = _id;
        this.life = _life;
        this.poison = _poison;
        this.energy = _energy;
        this.log = _log;
        this.experience = _experience;
        this.mana = mana;
    }

    //general
    public void handleLife(int _lifeChange) {
        if (life + _lifeChange <= 999 & life+_lifeChange>=0) {
            setLife(life + _lifeChange);
        }
        else if (life+_lifeChange>999){
            setLife(999);
        }
    }
    public void handleEnergy(int _energyChange){
        if(energy<99&energy+_energyChange>=0){
            setEnergy(energy+_energyChange);
        }
    }
    public void handlePoison(int _poisonChange){
        if(poison<10&poison+_poisonChange>=0){
            setPoison(poison+_poisonChange);
        }
    }

    public void handleExperience(int _experience) {
        if(energy<99&experience+_experience>=0){
            setExperience(experience+_experience);
        }
    }

    public void handleMana(int _manaChange, int _colorIndex){
        //increase/decrease mana based on given index:
        /*
        * w = 0
        * u = 1
        * b = 2
        * r = 3
        * g = 4
        * c = 5
        * */
        //if (mana[_colorIndex]+_manaChange>=0&mana[_colorIndex]+_manaChange<100){
            setMana(_colorIndex,mana[_colorIndex]+_manaChange);
        //}
    }
    public void addLog(String s) {
        setLog(s+log);
    }

    //getters and setters
    public int getId(){return id;}
    public void setId(int _id){this.id = _id; }

    public int getPoison() {
        return poison;
    }
    public void setPoison(int _poison) {
        this.poison = _poison;
    }

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }

    public int getMana(int i) {
        return mana[i];
    }
    public void setMana(int i, int _mana) {
        mana[i] = _mana;
    }

    public String getLog() {
        return log;
    }
    public void setLog(String log) {
        this.log = log;
    }


}
