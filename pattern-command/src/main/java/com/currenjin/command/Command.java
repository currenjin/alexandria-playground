package com.currenjin.command;

import com.currenjin.editor.Editor;

public abstract class Command {
    public Editor editor;
    private String backup;

    public Command(Editor editor) {
        this.editor = editor;
    }

    void backup() {
        this.backup = this.editor.textField.getText();
    }

    public void undo() {
        this.editor.textField.setText(this.backup);
    }

    public abstract boolean execute();
}
