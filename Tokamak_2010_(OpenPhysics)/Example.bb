Graphics3D 1280, 1024, 32, 2 
SetBuffer BackBuffer()
Include "PostProcess.bb"
AppTitle "Glow like Seyhajin" 
ClearTextureFilters

amb#=60
AmbientLight amb#,amb#,amb#

center		=	CreatePivot()
; -- !! Camera
Cam		=	CreateCamera()
				PositionEntity cam,0,0,-5
	initglow(Cam)
	fade#=0
	dark_passes=2
	glow_passes=3
	glare_size#=4
; -- !! lights
light2		=	CreateLight()
				PositionEntity light2,-38,10,-38				
; -- !! mesh
teapot		=	LoadMesh("teapot.x")			
tex			=	LoadTexture("spheremap.bmp",64)	
				EntityTexture teapot,tex
		
;main loop!
While Not KeyDown(1)
	TurnEntity teapot,0,1,.5
	PointEntity light2, center

	renderglow(fade#,dark_passes,glow_passes,glare_size#,255,255,100)
	UpdateWorld		
	RenderWorld 
	Color 155,255,200
	Text 20,20, "Glow like Seyhajin"
	Flip 1
	Wend 
End