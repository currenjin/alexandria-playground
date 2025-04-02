package com.currenjin.markandsweep;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 객체 간의 참조 관계를 나타내는 그래프 클래스
 */
public class ObjectGraph {
    private Set<Node> nodes;
    private Node root;

    /**
     * 새 객체 그래프를 생성합니다.
     */
    public ObjectGraph() {
        this.nodes = new HashSet<>();
        this.root = null;
    }

    /**
     * 그래프에 노드를 추가합니다.
     *
     * @param node 추가할 노드
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * 그래프의 루트 노드를 설정합니다.
     *
     * @param root 루트 노드
     */
    public void setRoot(Node root) {
        if (!nodes.contains(root)) {
            throw new IllegalArgumentException("Root node must be in the graph");
        }
        this.root = root;
    }

    /**
     * 그래프의 모든 노드를 반환합니다.
     *
     * @return 노드 집합
     */
    public Set<Node> getNodes() {
        return nodes;
    }

    /**
     * 그래프의 루트 노드를 반환합니다.
     *
     * @return 루트 노드
     */
    public Node getRoot() {
        return root;
    }

    /**
     * 루트에서 접근 가능한 모든 노드를 찾습니다.
     * DFS(깊이 우선 탐색) 알고리즘을 사용합니다.
     *
     * @return 접근 가능한 노드 집합
     */
    public Set<Node> findReachableNodes() {
        if (root == null) {
            return new HashSet<>();
        }

        // 모든 노드의 마킹 상태 초기화
        for (Node node : nodes) {
            node.unmark();
        }

        Set<Node> reachableNodes = new HashSet<>();
        Stack<Node> stack = new Stack<>();

        // 루트부터 시작
        stack.push(root);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            // 이미 처리한 노드는 건너뜀
            if (current.isMarked()) {
                continue;
            }

            // 현재 노드를 마킹하고 접근 가능 집합에 추가
            current.mark();
            reachableNodes.add(current);

            // 현재 노드가 참조하는 모든 노드를 스택에 추가
            for (Node reference : current.getReferences()) {
                if (!reference.isMarked()) {
                    stack.push(reference);
                }
            }
        }

        return reachableNodes;
    }
}