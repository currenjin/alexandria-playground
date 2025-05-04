import './App.css';
import { ForceGraph2D, ForceGraph3D } from 'react-force-graph';

const exampleCategories = [
  {
    id: 1,
    name: "category 1",
  },
  {
    id: 2,
    name: "category 2",
  },
]

const examplePosts = [
  {
    id: 1,
    categoryId: 1,
    title: "post 1",
    content: "test content",
  },
  {
    id: 2,
    categoryId: 1,
    title: "post 2",
    content: "test content",
  },
  {
    id: 3,
    categoryId: 1,
    title: "post 3",
    content: "test content",
  },
  {
    id: 4,
    categoryId: 2,
    title: "post 4",
    content: "test content",
  },
  {
    id: 5,
    categoryId: 2,
    title: "post 5",
    content: "test content",
  },
]


const getNodeIdForCurrentNode = (length, postId) => {
  return length + postId;
};

const getNodes = (categories, posts) => {
  const nodes = [];

  nodes.push(...categories.map(e => {
    return {
      "id": e.id,
      "name": e.name,
      "color": "green",
    };
  }));

  nodes.push(...posts.map(e => {
    return {
      "id": getNodeIdForCurrentNode(nodes.length, e.id),
      "name": e.title,
      "color": "black",
    };
  }));

  return nodes;
};

const getLinks = (categories, posts) => {
  return posts.map(e => {
    return {
      "source": getNodeIdForCurrentNode(categories.length, e.id),
      "target": e.categoryId,
    };
  });
};

const App = () => {

  const nodes = getNodes(exampleCategories, examplePosts);
  const links = getLinks(exampleCategories, examplePosts);

  const gData = {
    nodes,
    links,
  };

  return (
    <div className="App">
      <ForceGraph2D
        graphData={gData}
        backgroundColor={"white"}
        nodeLabel={node => node.name}
        nodeColor={node => node.color}
        linkColor={"black"}
      />
    </div>
  );
}

export default App;
