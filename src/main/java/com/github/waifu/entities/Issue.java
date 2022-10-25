package com.github.waifu.entities;

import com.github.waifu.enums.Problem;

/**
 *
 */
public class Issue {

    private Problem problem;
    private String whisper;
    private String message;

    /**
     *
     * @param problem
     */
    public Issue(Problem problem) {
        this.problem = problem;
        this.whisper = "None";
        this.message = "";
    }

    /**
     *
     * @return
     */
    public String getWhisper() {
        return this.whisper;
    }

    /**
     *
     * @param string
     */
    public void setWhisper(String string) {
        this.whisper = string;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param string
     */
    public void setMessage(String string) {
        switch(this.problem) {
            case UNDER_REQS -> this.message = "Under reqs, brought " + string;
            case EMPTY_SLOT -> this.message = "Under reqs, brought an " + string;
            case BANNED_ITEM -> this.message = "Under reqs, brought banned item " + string;
            case MISSING_REACT, MISSING_REACT_DPS -> this.message = "Fake reacting " + string;
            case SWAPOUT_ITEM -> this.message = "Under reqs, brought swapout item " + string + " without an item that meets reqs";
            case PRIVATE_PROFILE -> this.message = "Hiding character information";
            case MISSING_REACT_TRADE -> this.message = "Fake reacting " + string + ", didn't bring to the run";
            case NONE -> this.message = "";
        }
    }

    /**
     *
     * @return
     */
    public Problem getProblem() {
        return this.problem;
    }

    /**
     * setProblem method.
     *
     * Sets a problem if the level is higher.
     * If multiple problems exist, the one with the highest level is reported.
     *
     * @param problem contains the type of problem (Banned item/Under reqs/Swapout/etc)
     */
    public void setProblem(Problem problem) {
        if (this.problem != null) {
            if (this.problem.getLevel() >= problem.getLevel()) {
                this.problem = problem;
            }
        } else {
            this.problem = problem;
        }
    }
}
