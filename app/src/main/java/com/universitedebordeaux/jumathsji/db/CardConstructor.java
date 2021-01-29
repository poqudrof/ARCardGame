package com.universitedebordeaux.jumathsji.db;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.constructor.Constructor;

// SnakeYaml Constructor that builds a list of CardYaml from a YAML file.
public class CardConstructor extends Constructor {

    @Override
    protected Object constructObject(final Node node) {
        if (node instanceof SequenceNode && isRootNode(node)) {
            ((SequenceNode) node).setListType(CardYaml.class);
        }
        return super.constructObject(node);
    }

    private boolean isRootNode(final Node node) {
        return node.getStartMark().getIndex() == 0;
    }
}