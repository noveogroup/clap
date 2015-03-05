package com.noveogroup.clap.library.reporter;

public interface ClapReporter {

    public void reportInfo(Messages.Info message);

    public void reportCrash(Messages.Crash message);

}
