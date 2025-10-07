#include <stdio.h>
#include <stdlib.h>

typedef struct ListNode {
    int data;
    struct ListNode* link;
} listNode;

typedef struct {
    listNode* head;
} linkedList_h;

linkedList_h* create(void) {
    linkedList_h *h = malloc(sizeof(linkedList_h));
    h -> head = NULL;
    return h;
}

void addNode(linkedList_h* h, const int n) {
    listNode *newNode = malloc(sizeof(listNode));
    newNode -> data = n;
    newNode -> link = NULL;

    if (h -> head == NULL) {
        h -> head = newNode;
        return;
    }

    listNode *lastNode = h->head;
    while (lastNode -> link != NULL) lastNode = lastNode -> link;
    lastNode -> link = newNode;
}

void addItNode(listNode * prev, int n) {
    listNode *newNode = malloc(sizeof(listNode));
    newNode -> data = n;
    newNode -> link = prev -> link;

    prev -> link = newNode;
};

void deleteNode(linkedList_h* h) {
    if (h -> head == NULL) return;
    if (h -> head -> link == NULL) {
        free(h -> head);
        h -> head = NULL;
        return;
    }

    listNode *prevNode = h -> head;
    listNode *delNode = prevNode -> link;

    while (delNode -> link != NULL) {
        prevNode = delNode;
        delNode = delNode -> link;
    }

    free(delNode);
    prevNode -> link = NULL;
}


void deleteItNode(listNode *prevNode, listNode *delNode) {
    prevNode -> link = delNode -> link;
    free(delNode);
}

void deleteItData(linkedList_h* h, const int n) {
    if (h -> head == NULL) return;

    listNode *prevNode = h;
    listNode *delNode = h -> head;

    while (delNode != NULL) {
        if (delNode -> data == n) {
            deleteItNode(prevNode, delNode);
            return;
        }

        prevNode = delNode;
        delNode = delNode -> link;
    }
}

int main() {
    linkedList_h* h = create();

    addNode(h, 10);
    addNode(h, 20);
    addNode(h, 40);
    addItNode(h -> head -> link, 30);
    deleteNode(h);
    addNode(h, 50);
    deleteItData(h, 30);
    deleteItNode(h -> head, h -> head -> link);

    const listNode* current = h -> head;
    while (h -> head != NULL) {
        printf("item: %d\n", current -> data);
        current = current -> link;
    }

    free(h);
}
