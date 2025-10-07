#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    struct node *left;
    struct node *right;
    char data;
} node;

node* create(void) {
    node *root = malloc(sizeof(node));
    return root;
}

void preOrder(const node* root) {
    if (root != NULL) {
        printf("%c ", root -> data);
        preOrder(root -> left);
        preOrder(root -> right);
    }
}

int main() {
    node *node = create();

    preOrder(node);

    free(node);
}
