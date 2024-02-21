Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example03 / Materials"
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
			PositionEntity cam,45,45,-45
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

Material1=opCreateMaterial(1,1.00,1.00);id,Friction#,Restitution#
Material2=opCreateMaterial(2,0.75,0.75)
Material3=opCreateMaterial(2,0.50,0.50)
Material4=opCreateMaterial(2,0.25,0.25)
Material5=opCreateMaterial(3,0.10,0.10)
; create static rotated plane
static.tok=	opBodyCreateBox(40, .5, 40,0);mass 0 = static body
			opBodySetPos(static\body,0,18,0)
			opBodySetRot(static\body,-35,0,0)
			opBodySetGfx(static\mesh,checktex2)
			opSetMaterial(static\geom,Material1)			
; Friction test
obj.tok	=	opBodyCreateBox(2,1,4,1)
			opBodySetPos(obj\body,	-15,25,5)
			opBodySetGfx(obj\mesh,checktex,255, 150,150)
			opSetMaterial(obj\geom,Material1)						
obj.tok	=	opBodyCreateBox(2,1,4,1)
			opBodySetPos(obj\body,	-7,25,5)
			opBodySetGfx(obj\mesh,checktex,255, 255,150)
			opSetMaterial(obj\geom,Material2)			
obj.tok	=	opBodyCreateBox(2,1,4,1)
			opBodySetPos(obj\body,	0,25,5)
			opBodySetGfx(obj\mesh,checktex,150,255,150)
			opSetMaterial(obj\geom,Material3)	
obj.tok	=	opBodyCreateBox(2,1,4,1)
			opBodySetPos(obj\body,	7,25,5)
			opBodySetGfx(obj\mesh,checktex,150,255,255)
			opSetMaterial(obj\geom,Material4)	
obj.tok	=	opBodyCreateBox(2,1,4,1)			
			opBodySetPos(obj\body,	15,25,5)
			opBodySetGfx(obj\mesh,checktex,150,150,255)
			opSetMaterial(obj\geom,Material5)			
			
; to test elasticity
obj.tok	=	opBodyCreateSphere(2,1)
			opBodySetPos(obj\body,	30,25,-15)
			opBodySetGfx(obj\mesh,checktex,255, 150,150)
			opSetMaterial(obj\geom,Material1)	
obj.tok	=	opBodyCreateSphere(2,1)
			opBodySetPos(obj\body,	30,25,-7)
			opBodySetGfx(obj\mesh,checktex,255, 255,150)
			opSetMaterial(obj\geom,Material2)	
obj.tok	=	opBodyCreateSphere(2,1)
			opBodySetPos(obj\body,	30,25,0)
			opBodySetGfx(obj\mesh,checktex,150,255,150)
			opSetMaterial(obj\geom,Material3)				
obj.tok	=	opBodyCreateSphere(2,1)
			opBodySetPos(obj\body,	30,25,7)
			opBodySetGfx(obj\mesh,checktex,150,255,255)
			opSetMaterial(obj\geom,Material4)	
obj.tok	=	opBodyCreateSphere(2,1)
			opBodySetPos(obj\body,	30,25,15)
			opBodySetGfx(obj\mesh,checktex,150,150,255)	
			opSetMaterial(obj\geom,Material5)	
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