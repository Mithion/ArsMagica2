package am2.render3d;

import am2.AMCore;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OBJModel{
	private OBJTexCoord[] textureCoords;
	private OBJNormal[] normals;
	private OBJVertex[] vertices;
	private OBJFace[] faces;

	private boolean loaded;
	private boolean flatShading;
	private int cullMode;

	private int glCallList;

	public OBJModel(ResourceLocation objFile, boolean flatShading){
		loaded = LoadOBJModel(objFile);
		if (!loaded){

		}
		this.flatShading = flatShading;
		this.cullMode = GL11.GL_FRONT;
	}

	private void createMissingModel(){
		this.vertices = new OBJVertex[8];

		this.vertices[0] = new OBJVertex(0, 0, 0);
		this.vertices[1] = new OBJVertex(1, 0, 0);
		this.vertices[2] = new OBJVertex(0, 0, 1);
		this.vertices[3] = new OBJVertex(1, 0, 1);
		this.vertices[4] = new OBJVertex(0, 1, 0);
		this.vertices[5] = new OBJVertex(1, 1, 0);
		this.vertices[6] = new OBJVertex(0, 1, 1);
		this.vertices[7] = new OBJVertex(1, 1, 1);

		this.textureCoords = new OBJTexCoord[1];
		this.textureCoords[0] = new OBJTexCoord(0, 0);


		this.faces = new OBJFace[6];

		this.faces[0] = new OBJFace(new int[]{}, new int[]{0}, new int[4]);

		this.calculateNormals();
	}

	public OBJModel(ResourceLocation objFile){
		this(objFile, false);
	}

	public OBJModel SetBackCulling(){
		this.cullMode = GL11.GL_BACK;
		return this;
	}

	public boolean IsLoaded(){
		return loaded;
	}

	private InputStream getResourceAsStream(String resourceName){
		return AMCore.class.getResourceAsStream(resourceName);
	}

	private boolean LoadOBJModel(ResourceLocation path){

		InputStream stream = getResourceAsStream(path.getResourcePath());

		if (stream == null) return false;

		ArrayList<String> lines = new ArrayList<String>();

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null){
				lines.add(line);
			}
			br.close();
			stream.close();
		}catch (Throwable t){
			AMCore.log.error("Error reading OBJ File Data!");
			return false;
		}

		if (parseOBJFileLines(lines)){
			/*AMCore.log.info("Loaded Model " + path);
			AMCore.log.info("Vertices: " + vertices.length);
			AMCore.log.info("Texture Coords: " + textureCoords.length);
			AMCore.log.info("Normals: " + normals.length);
			AMCore.log.info("Faces: " + faces.length);*/

			return true;
		}else{
			return false;
		}

	}

	private boolean parseOBJFileLines(ArrayList<String> lines){

		ArrayList<OBJTexCoord> textureCoords = new ArrayList<OBJTexCoord>();
		ArrayList<OBJNormal> normals = new ArrayList<OBJNormal>();
		ArrayList<OBJVertex> vertices = new ArrayList<OBJVertex>();
		ArrayList<OBJFace> faces = new ArrayList<OBJFace>();

		for (String line : lines){
			String[] sections = line.split(" ");
			if (line.startsWith("vn ")){ //normals				
				normals.add(new OBJNormal(Float.parseFloat(sections[1]), Float.parseFloat(sections[2]), Float.parseFloat(sections[3])));
			}else if (line.startsWith("vt ")){ //uv							
				textureCoords.add(new OBJTexCoord(Float.parseFloat(sections[1]), Float.parseFloat(sections[2])));
			}else if (line.startsWith("v ")){ //vertex				
				vertices.add(new OBJVertex(Float.parseFloat(sections[1]), Float.parseFloat(sections[2]), Float.parseFloat(sections[3])));
			}else if (line.startsWith("f ")){
				//face
				ArrayList<Integer> v = new ArrayList<Integer>();
				ArrayList<Integer> t = new ArrayList<Integer>();
				ArrayList<Integer> n = new ArrayList<Integer>();

				for (String s : sections){
					if (s.equals("f")) continue;
					String[] vtn = s.split("/");
					v.add(Integer.parseInt(vtn[0]));
					if (vtn.length > 1){
						if (vtn[1].equals("")) t.add(0);
						else t.add(Integer.parseInt(vtn[1]));
					}else{
						t.add(0);
						n.add(0);
					}
					if (vtn.length > 2){
						n.add(Integer.parseInt(vtn[2]));
					}else{
						n.add(0);
					}
				}
				int[] iv = toArray(v);
				int[] it = toArray(t);
				int[] in = toArray(n);

				faces.add(new OBJFace(iv, it, in));
			}
		}

		this.textureCoords = new OBJTexCoord[textureCoords.size()];
		this.normals = new OBJNormal[normals.size()];
		this.vertices = new OBJVertex[vertices.size()];
		this.faces = new OBJFace[faces.size()];

		this.textureCoords = textureCoords.toArray(this.textureCoords);
		this.normals = normals.toArray(this.normals);
		this.vertices = vertices.toArray(this.vertices);
		this.faces = faces.toArray(this.faces);

		calculateNormals();

		PrepareGLCallList();

		return true;
	}

	private void calculateNormals(){

		//calculate face normals
		for (OBJFace face : this.faces){
			if (face.faceNormal != null) continue;
			OBJNormal faceNormal = calculateFaceNormal(face);
			face.setNormal(faceNormal);
		}

		if (!flatShading){
			//calculate vertex normals as the average of adjacent face normals
			for (int i = 0; i < this.vertices.length; ++i){
				calculateVertexNormal(i);
			}
		}
	}

	private OBJNormal calculateFaceNormal(OBJFace face){
		OBJNormal normal = new OBJNormal(0, 0, 0);

		for (int i = 0; i < face.v.length; ++i){
			OBJVertex v = this.vertices[face.v[i] - 1];
			OBJVertex vn = this.vertices[face.v[(i + 1) % face.v.length] - 1];

			normal.i = normal.i + ((v.y - vn.y) * (v.z - vn.z));
			normal.j = normal.j + ((v.z - vn.z) * (v.x - vn.x));
			normal.k = normal.k + ((v.x - vn.x) * (v.y - vn.y));
		}

		normal.normalize();
		//normal.flip();

		return normal;
	}

	private OBJNormal calculateVertexNormal(int index){
		int[] adjoiningFaces = new int[this.faces.length];

		int numAdjoiningFaces = 0;
		for (int i = 0; i < this.faces.length; ++i){
			OBJFace face = this.faces[i];
			for (int j = 0; j < face.v.length; ++j){
				if (face.v[j] == index){
					adjoiningFaces[numAdjoiningFaces++] = i;
					break;
				}
			}
		}

		OBJNormal normal = new OBJNormal(0, 0, 0);

		for (int i = 0; i < numAdjoiningFaces; ++i){
			OBJFace face = this.faces[i];
			if (face.faceNormal != null){
				normal.i += face.faceNormal.i;
				normal.j += face.faceNormal.j;
				normal.k += face.faceNormal.k;
			}else{
				//TODO: point normals where face normal doesn't exist (really shouldn't need this though for now)
			}
		}

		normal.i /= numAdjoiningFaces;
		normal.j /= numAdjoiningFaces;
		normal.k /= numAdjoiningFaces;

		normal.normalize();

		return normal;
	}

	private int[] toArray(ArrayList<Integer> iArr){
		int[] arr = new int[iArr.size()];
		for (int i = 0; i < iArr.size(); ++i){
			arr[i] = iArr.get(i);
		}
		return arr;
	}

	public void PrepareGLCallList(){
		glCallList = GL11.glGenLists(1);
		GL11.glNewList(glCallList, GL11.GL_COMPILE);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glCullFace(GL11.GL_FRONT);

		for (int i = 0; i < faces.length; ++i){
			OBJFace face = faces[i];
			RenderFace(face);
		}


		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glEndList();
	}

	public void RenderOBJModel(Tessellator t){
		GL11.glCallList(glCallList);
	}

	private void RenderFace(OBJFace face){
		int[] v = face.v;
		int[] tc = face.t;
		int[] n = face.n;

		boolean vertexNormals = true;

		GL11.glBegin(GL11.GL_TRIANGLES);

		if (this.flatShading){
			if (face.faceNormal != null){
				OBJNormal normal = face.faceNormal;
				if (Float.isNaN(normal.i) || Float.isNaN(normal.j) || Float.isNaN(normal.k)){
					vertexNormals = true;
				}else{
					GL11.glNormal3f(normal.i, normal.j, normal.k);
					vertexNormals = false;
				}
			}
		}else{
			vertexNormals = true;
		}

		for (int i = 0; i < v.length; ++i){
			OBJVertex vertex = this.vertices[v[i] - 1];
			OBJNormal normal = null;
			OBJTexCoord texCoord = null;

			if (tc[i] != 0){
				texCoord = this.textureCoords[tc[i] - 1];
				GL11.glTexCoord2f(texCoord.u, texCoord.v);
			}
			if (vertexNormals){
				if (n[i] != 0){
					normal = this.normals[n[i] - 1];
					GL11.glNormal3f(normal.i, normal.j, normal.k);
				}
			}

			GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
		}

		GL11.glEnd();
	}
}
