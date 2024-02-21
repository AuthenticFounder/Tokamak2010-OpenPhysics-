Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example13 / ConvexHull"
AntiAlias True
SetBuffer BackBuffer()
SeedRnd MilliSecs()

center	=	CreatePivot()
cam		=	CreateCamera()
			CameraClsColor cam,255,255,255
			CameraFogColor cam,255,255,255
			CameraFogMode cam,1
			CameraFogRange cam,50,200
			CameraRange cam,.2,100000
			CreateListener Cam, .1
			PositionEntity cam,-30,30,-30
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
Global checktex1,checktex2,checktex
		
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

; Create siple Convex body
file$	=	"chevelle.3ds"
Model	=	LoadMesh(file$)
			ScaleMesh Model,3,3,3
			HideEntity Model

;generate convex hull, so that it could be copyed when doing more than one body
Convex	=	opBodyGenerateConvex(Model,file$)

; Create a patch of convex hull's
For i = 1 To 20
	obj.tok	=	opBodyCreateHull(Model,Convex,10)
	obj\mesh=	CopyEntity(Model)
				opBodySetPos(obj\body,Rnd(-15,15),Rnd(15,20),Rnd(-15,15))
				opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
				opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
				opBodySetGfx(obj\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))
Next

;Note! If you only want to do a single convex hull you can also use this function:
;obj.tok	=	opBodyMakeHull(Model,file$,1)

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
		
	
	; update physics & Position physic Box to Entity	
	pms = MilliSecs()	
	opWorldStep(0.025)
	Pyhsic_time = MilliSecs() - pms	
	
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
	
	Flip 1

Wend
opDestroyWorld()
End