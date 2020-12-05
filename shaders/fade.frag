#version 330 core

layout (location = 0) out vec4 color;

uniform float time;

void main() 
{
	if(time > 1.0)
		discard;
	color = vec4(0.3, 0.3, 0.3, 1.0 - time);
}