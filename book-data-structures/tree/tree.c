#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    struct node *left;
    struct node *right;
    char data;
} node;

node* create(const char data) {
    node* newNode = malloc(sizeof(node));
    newNode -> data = data;
    newNode -> left = NULL;
    newNode -> right = NULL;
    return newNode;
}

node *insert(node *here, node *it) {
    if (here == NULL) {
        here = it;
        return here;
    }

    if (here -> left == NULL) {
        here -> left = it;
        return here;
    }

    here -> right = it;
    return here;
}

node *searchParent(node *root, node *it) {
    if (root == NULL || it == NULL) return NULL;

    if (root -> left == it || root -> right == it) return root;

    node* found = searchParent(root -> left, it);
    if (found != NULL) return found;

    return searchParent(root -> right, it);
}

node *delete(node *root, node *it, const char direction) {
    node *parent = searchParent(root, it);

    if (parent == NULL) {
        printf("Cannot delete\n");
        return NULL;
    }

    if (direction == 'L') {
        parent -> left = NULL;
        free(parent -> left);
        return it;
    }

    if (direction == 'R') {
        parent -> right = NULL;
        free(parent -> right);
        return it;
    }

    return NULL;
}

void preOrder(const node* root) {
    if (root != NULL) {
        printf("%c ", root -> data);
        preOrder(root -> left);
        preOrder(root -> right);
    }
}

void inOrder(const node *root) {
    if (root != NULL) {
        inOrder(root -> left);
        printf("%c ", root -> data);
        inOrder(root -> right);
    }
}

void postOrder(const node *root) {
    if (root != NULL) {
        postOrder(root -> left);
        postOrder(root -> right);
        printf("%c ", root -> data);
    }
}

int getNodeCount(node *root) {
    int num = 0;
    if (root == NULL) return num;

    num = 1;
    num += getNodeCount(root -> left) + getNodeCount(root -> right);
    return num;
}

int main() {
    node* a = create('A');
    node* b = create('B');
    node* c = create('C');
    node* d = create('D');
    node* e = create('E');

    insert(b, d);
    insert(c, e);
    insert(a, b);
    insert(a, c);

    const int firstNodeCount = getNodeCount(a);
    printf("nodeCount: %d\n", firstNodeCount);

    printf("preOrder: ");
    preOrder(a);
    printf("\n");

    printf("inOrder: ");
    inOrder(a);
    printf("\n");

    printf("postOrder: ");
    postOrder(a);
    printf("\n");

    delete(a, e, 'L');
    printf("preOrder: ");
    preOrder(a);
    printf("\n");

    const int secoundNodeCount = getNodeCount(a);
    printf("nodeCount: %d\n", secoundNodeCount);

    free(a);
}
