# faiss_server.py
from flask import Flask, request, jsonify
import faiss
import numpy as np

app = Flask(__name__)
index = None
data_map = {}

@app.route("/index", methods=["POST"])
def index_vector():
    global index
    vectors = np.array(request.json["vectors"]).astype('float32')
    ids = request.json["ids"]

    index = faiss.IndexFlatL2(vectors.shape[1])
    index.add(vectors)
    for i, id in enumerate(ids):
        data_map[i] = id
    return jsonify({"status": "indexed", "size": len(data_map)})

@app.route("/search", methods=["POST"])
def search():
    try:
        print("📥 Nhận request search")
        query = np.array(request.json["query"]).astype('float32')
        print("🔍 query shape:", query.shape)
        k = request.json.get("top_k", 3)
        D, I = index.search(query, k)
        results = [data_map[int(i)] for i in I[0]]
        return jsonify(results)
    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500



app.run(host="0.0.0.0", port=8000)
