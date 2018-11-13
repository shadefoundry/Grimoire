package shadefoundry.grimoire;

public class CommanderDamage {
    String opponentName;
    int damageTaken;
    public CommanderDamage(String _opponentName,int _damageTaken){
        opponentName=_opponentName;
        damageTaken=_damageTaken;
    }
    public String getOpponentName(){
        return opponentName;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int _damageTaken) {
        this.damageTaken = _damageTaken;
    }

    public void setOpponentName(String _opponentName) {
        this.opponentName = _opponentName;
    }
}
