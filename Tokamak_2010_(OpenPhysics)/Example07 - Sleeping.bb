Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example07 / Sleeping"

SetBuffer BackBuffer()
SeedRnd MilliSecs()

center	=	CreatePivot()
cam		=	CreateCamera()
			CameraClsColor cam,255,255,255
			CameraFogColor cam,255,255,255
			CameraFogMode cam,1
			CameraFogRange cam,50,200
			CameraRange cam,.2,100000
			PositionEntity cam,35,25,-35
			PointEntity cam,center
			
			EntityRadius cam,1
			EntityType cam,COLL_CAM	
			opSetLogo(cam)
		
			AmbientLight 50,42,39
light1	=	CreateLight()
			LightColor light1,228,224,187
			RotateEntity light1,20,50,0
light2	=	CreateLight()
			LightColor light2,88,126,147
			RotateEntity light2,-45,180+45,0			

;textures			
checktex1=	CreateCheckerTex()
			ScaleTexture checktex1,5,5

checktex2=	CreateCheckerTex()
			ScaleTexture checktex2,.5,.5
						
checktex=	CreateCheckerTex()
			ScaleTexture checktex,1,1
			
; Create mirror plane
plane	=	CreatePlane(16)
			opBodySetGfx(plane,checktex1)
			EntityAlpha plane,.8
			CreateMirror

; Create physic world
opWorldCreate(1)

; Create Pyramid wall
size=30
Spacing = 0
Cubesize=1
pos_y = 1
Offset = -size * (Cubesize*2 + Spacing) * 0.5
While size
		For  i=0 To size-1

				Pos_x = Offset + i * (CubeSize*2 + Spacing)					
				box.tok	=	opbodyCreateBox(CubeSize*2,CubeSize*2,CubeSize*2,1)					
   				opBodySetPos(box\body,pos_x, pos_y,0)
				opBodySetGfx(box\mesh,checktex)					
				opBodySetAutoSleep(box\body,True)
				opBodySetAutoSleepTreshold(box\body,1)

		Next
	Offset = Offset +Cubesize;
	pos_y =  pos_y +(Cubesize*2 + Spacing)
	size=size-1
Wend 

; Create bullet
bullet.tok	=	opBodyCreateSphere(3,100)
				opBodySetPos(bullet\body,0,10,-150)
				opBodySetVel(bullet\body,0,0,50)
				opBodySetGfx(bullet\mesh,checktex)

;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3

;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------
	
	
	;Move The camera
	FreeLook(cam,.1)
	
	;Apply Random Force
	If KeyHit(57)
		For p.tok = Each tok
			TOKRB_ApplyImpulse(p\body, 0, Rnd(10, 20), 0)
			TOKRB_ApplyTwist p\body, Rnd(-5, 5), Rnd(-5, 5), Rnd(-5, 5)
		Next		
	EndIf
	
	;show sleep
	For box = Each tok
		If opBodyGetSleep(box\body)=1
		EntityColor box\mesh,255,200,150
		Else
		EntityColor box\mesh,0,150,200
		End If
	Next
	
	; update physics & Position physic Box to Entity
	pms = MilliSecs()	
	If MouseDown(2)
		opWorldStep(0.003)
	Else
		opWorldStep(0.025)
	End If
	Pyhsic_time = MilliSecs() - pms
	;temp
	;opGeomSetEnt(geom,mesh)
	
	; Render world
	ms = MilliSecs()
	UpdateWorld
	RenderWorld
	Render_time = MilliSecs() - ms

	Color 50,50,50
	Text 20,20,"FPS = "+Fps(50)
	Text 20,40,"Physics = "+Pyhsic_time
	Text 20,60,"Render = "+Render_time
	Text 20,80,"bodys = "+opBodyCount	
	Text 20,100,"RightMouse - Slow Motion"
	
	Flip 1

Wend
opDestroyWorld()
End