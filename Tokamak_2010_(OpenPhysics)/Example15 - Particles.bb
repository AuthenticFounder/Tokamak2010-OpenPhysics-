Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example15 / Particles."
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
			PositionEntity cam,18,25,17
			;RotateEntity cam,0,-110,0
			
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
						
checktex	=	CreateCheckerTex()
			ScaleTexture checktex,1,1
			
; Create mirror plane
plane	=	CreatePlane(16)
			opBodySetGfx(plane,checktex1)
			;EntityAlpha plane,.8
			;CreateMirror

; Create physic world
opWorldCreate(1)

; type for smoke
Type particle
	Field body,geom
	Field sprite
	Field life
End Type

;a body to affect the particle
static.tok	=	opBodyCreateCapsule(4,10,0)
				opBodySetPos(static\body,0,5,10)
				opBodySetRot(static\body,0,0,0)
				opBodySetGfx(static\mesh,checktex2,200,255,100)

;create a uarticle emmiter
Global Emitter=	CreateCylinder()
				ScaleEntity emitter,5,1,5
				PositionEntity emitter,0,1,0
				opBodySetGfx(emitter,checktex2,200,255,100)			
; load a smoke sprite
Global SmokeSprite	=	LoadSprite("smoke.jpg",3)
HideEntity smokesprite
Global ParticleLife#=300
Function CreateParticle()
	size=4
	j.particle = New particle
	
	j\body = TOKRB_Create()
	j\geom = TOKRB_AddSphere(j\body,size)
	TOKRB_SetLinearDamping j\body,0.0
	TOKRB_SetAngularDamping j\body,0.0
	TOKRB_SetMass j\body,.1
	TOKRB_SetSphereInertiaTensor j\body,size,.1
	TOKRB_SetCollisionID(j\body,TOK_COLL_PARTICLE)
	
	opBodySetPos(j\body,EntityX(Emitter)+Rnd(-1,1),EntityY(Emitter),EntityZ(Emitter)+Rnd(-1,1))
	;TOKRB_GravityEnable(j\body,0)
	
	;opBodySetWater(j\body,10,110,0,0,1,0,1.125,0,0)
	opBodySetVel(j\body,Rnd(-.5,.5),Rnd(-.5,.5),Rnd(-.5,.5))	
	opParticleCount=opParticleCount+1
	
	j\sprite = CopyEntity(SmokeSprite)
	EntityColor j\sprite,100,255,200
	ScaleSprite j\sprite,2,2
	EntityBlend j\sprite,1
End Function

;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3


;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------
	; create new particles
	t = t+1
	; create new smoke
	n=1-n
	If n=1 CreateParticle()
	;CreateParticle()
		



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
	
	; Update
	For j.particle = Each particle
		j\life = j\life+1
		EntityAlpha j\sprite,.5-j\life/(ParticleLife*2)
		PositionEntity j\sprite,TOKRB_GetX(j\body),TOKRB_GetY(j\body),TOKRB_GetZ(j\body)
		TOKRB_SetForce(j\body,0,.5,.2)
		ScaleSprite j\sprite,j\life/50.0+2,j\life/50.0+2
		If j\life>ParticleLife Then
			;this part is very important to first deactivate the rigid body's and then after a tick remove the body
			TOKRB_Active j\body, False
			opBodyDestroy(j\body,j\geom)
			FreeEntity j\sprite
			Delete j
			opParticleCount=opParticleCount-1
		EndIf		
	Next
		
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
	Text 20,100,"Particles = "+opParticleCount	
	Text 20,120,"Cam pos X: "+EntityX(cam)+" Y: "+EntityY(cam)+" Z: "+EntityZ(cam)
	Flip 0

Wend
opDestroyWorld()
End