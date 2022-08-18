package com.github.waifu.entities;

import com.github.waifu.enums.Problem;

public class Issue {

    private Problem problem;
    private String whisper;
    private String message;

    public Issue(Problem problem) {
        this.problem = problem;
        this.whisper = "";
        this.message = "";
    }

    public String getWhisper() {
        return this.whisper;
    }

    public void setWhisper(String string) {
        switch(this.problem) {
            case UNDER_REQS -> this.whisper = string + " is not reqs. Please swap to another item";
            case EMPTY_SLOT -> this.whisper = "Please equip an item that meets reqs in your empty slot";
            case BANNED_ITEM -> this.whisper = string + " is a banned item. Please swap to another that meets reqs";
            case MISSING_REACT -> this.whisper = "Please equip " + string + " so I can confirm it. You can swap it out after";
            case MISSING_REACT_DPS -> this.whisper = "Please equip your " + string + " dps set so I can confirm it. You can swap it out after";
            case SWAPOUT_ITEM -> this.whisper = string + " is a swapout item. Please equip an item that meet reqs so I can confirm it. You can swap it out after";
            case PRIVATE_PROFILE -> this.whisper = "No Whisper";
            case MISSING_REACT_TRADE -> this.whisper = "Please trade me so I can confirm your " + string;
            case NONE -> this.whisper = "No message";
        }
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String string) {
        switch(this.problem) {
            case UNDER_REQS -> this.message = "Under reqs, brought " + string;
            case EMPTY_SLOT -> this.message = "Under reqs, brought an " + string;
            case BANNED_ITEM -> this.message = "Under reqs, brought banned item " + string;
            case MISSING_REACT -> this.message = "Fake reacting " + string;
            case MISSING_REACT_DPS -> this.message = "Fake reacting " + string;
            case SWAPOUT_ITEM -> this.message = "Under reqs, brought swapout item " + string + " without an item that meets reqs";
            case PRIVATE_PROFILE -> this.message = "Hiding character information";
            case MISSING_REACT_TRADE -> this.message = "Fake reacting " + string + ", didn't bring to the run";
            case NONE -> this.message = "No message";
        }
    }

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
