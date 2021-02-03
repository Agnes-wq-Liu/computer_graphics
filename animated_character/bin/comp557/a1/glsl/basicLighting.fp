#version 400
//Agnes Liu 260713093
uniform vec3 ks;
uniform vec3 kd;

uniform vec3 Ambient;
uniform vec3 lightDir1;
uniform vec3 lightColor1;

uniform vec3 lightDir2;
uniform vec3 lightColor2;

uniform vec3 lightDir3;
uniform vec3 lightColor3;


uniform float Shininess;

in vec3 normalForFP;
in vec3 viewDir;

out vec4 fragColor;

// TODO: Objective 7, GLSL lighting

void main(void) {
//First light direction
	vec3 HalfVector1 = normalize(lightDir1 + viewDir);
	float sp1 = max(0.0, dot(normalForFP, HalfVector1));
	float diffuse1 = max(0.0, dot(normalForFP, lightDir1));
	vec3 scatterLight1 = lightColor1 * diffuse1;
	if (diffuse1 == 0.0){
		sp1 = 0.0;
	}else{
		sp1 = pow(sp1, Shininess);
	}
	vec3 refLight1 = lightColor1 * sp1;

//Second light direction
	vec3 HalfVector2 = normalize(lightDir2 + viewDir);
	float sp2 = max(0.0, dot(normalForFP, HalfVector2));
	float diffuse2 = max(0.0, dot(normalForFP, lightDir2));
	vec3 scatterLight2 = lightColor2 * diffuse2;
	if (diffuse2 == 0.0){
		sp2 = 0.0;
	}else{
		sp2 = pow(sp2, Shininess);
	}
	vec3 refLight2 = lightColor2 * sp2;
	
//Third light direction
	vec3 HalfVector3 = normalize(lightDir3 + viewDir);
	float sp3 = max(0.0, dot(normalForFP, HalfVector3));
	float diffuse3 = max(0.0, dot(normalForFP, lightDir3));
	vec3 scatterLight3 = lightColor3 * diffuse3;
	if (diffuse3 == 0.0) sp3 = 0.0;
		else sp3 = pow(sp3, Shininess);
	vec3 refLight3 = lightColor3 * sp3;
	
	vec3 rgb = Ambient+  kd * (scatterLight1 + scatterLight2 + scatterLight3) + ks * (sp1 + sp2 + sp3);

	fragColor = vec4(rgb, 1);
}

