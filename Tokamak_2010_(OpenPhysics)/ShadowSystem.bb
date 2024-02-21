;Created by smr597, but mostly this is based on Mikkel Fredborg's "Realtime Shadow thingy"
;Projectional Shadow Mapping, it still needs few functions such as, delete caster and so on... but is good for fast prototyping

;Some info on real shadow mapping...
;Shadow Mapping = The process of comparing wether a pixel is visible from the light source by comparing it 
;To a depth image of the lightsource view stored in the form of a texture.
;Steps in creating And applying a shadow map
;1 - Render the scene in shadow And store the image as a texture.
;2 - Do a depth test To see If pixels are visible Or Not.
;3 - Render the scene lit, areas that failed the depth test will Not be re-rendered.

Include "fps.bb"

; ---> TYPES <---
Type caster
	Field mesh
End Type

Type dcaster
	Field mesh
End Type

Type receiver
	Field mesh
	Field brush,brushorig
	Field texure0,texure1,texure2,texure3,texure4,texure5,texure6,texure7
	Field surf
	Field orig
End Type

Type light
	Field entity,camera
	Field width,height
	Field typ,ShadowRange#
	Field lastx#,lasty#,lastz#
	Field lastpitch#,lastyaw#,lastroll#
End Type

; ---> GLOBALS <---
Global ShadowBrush,ShadowTex,LitTex,ClearTex
Global ShadowTexSize=512;<-shadow mapp size (tip: for 1024x768 use 512)
Global SoftShadow=True
Global SoftShadowQuality=2
Global SoftShadowRadius=2
Global ShadowScale#=10;3.0
Global ShadowRange#=4000;<-default shadow range
Global ShadowAR#=100,ShadowAG#=100,ShadowAB#=100;<-default shadow color


; ---> FUNCTIONS <---
;start shadowsystem
Function InitShadowSystem()

	;ClearTextureFilters
	ShadowTex = CreateTexture(ShadowTexSize,ShadowTexSize,1+16+32+256)
	TextureBlend ShadowTex,3
	
	;
	ShadowTempTex=CreateTexture(64,64)
	SetBuffer TextureBuffer(ShadowTempTex)
	Color 0,0,0
	Rect 0,0,64,64,1
	SetBuffer BackBuffer()
	TextureBlend ShadowTempTex,1
	
	ShadowBrush = CreateBrush()
	BrushBlend ShadowBrush,5
	BrushFX ShadowBrush,1
	BrushTexture ShadowBrush,ShadowTempTex,0,0	
	BrushTexture ShadowBrush,ShadowTex,0,1			
	; Make a blank white texture, for shadow receiver	
	ClearTex=CreateTexture(64,64)
	SetBuffer TextureBuffer(ClearTex)
	Color 255,255,255
	Rect 0,0,64,64,1
	SetBuffer BackBuffer()
	TextureBlend ClearTex,1

	
End Function


;Set Light
Function SetLight(entity,typ=1,range#=0)

	dll.light = New light
	
	dll\camera	 = CreateCamera(entity)
	CameraProjMode dll\camera,0
	CameraRange dll\camera,0.1,range#
	CameraFogMode dll\camera,1
	CameraFogRange dll\camera,0,0
	;CameraFogColor dll\camera,255,255,255
	CameraFogColor dll\camera,ShadowAR#,ShadowAG#,ShadowAB#
	;CameraClsColor dll\camera,50,50,50;60,60,80
	CameraClsColor dll\camera,255,255,255
	CameraClsMode dll\camera, 1, 1
	;CameraClsMode dll\camera, 1, 1
	CameraViewport dll\camera,0,0,ShadowTexSize,ShadowTexSize
	;CameraZoom dll\camera,ShadowScale ;<--- This may need changing depending on the scene and light position
	
	If SoftShadow ShadowScale#=ShadowScale#/2			
	dll\entity 	 = entity
	If range#<1 Then range#=ShadowRange#
	dll\ShadowRange#= range#
	dll\typ = typ
	;ClearTextureFilters
				
End Function

;Set Shadow Caster
Function SetCaster(mesh,Trans = 0)	
	caster=CopyEntity(mesh,mesh)
	If Trans = 0
		;EntityTexture caster,ShadowColorTex,0,0
		;EntityTexture caster,ShadowColorTex,0,1
		;EntityTexture caster,ShadowColorTex,0,2
		;EntityTexture caster,ShadowColorTex,0,3
		;EntityTexture caster,ShadowColorTex,0,4
		;EntityTexture caster,ShadowColorTex,0,5
		;EntityTexture caster,ShadowColorTex,0,6
		;EntityTexture caster,ShadowColorTex,0,7
		;EntityColor caster,AR#,AG#,AB#	
		;EntityFX caster,1+8;+16
		;max_surfs = CountSurfaces(caster)
		;For su = 1 To max_surfs
			;CurSurface = GetSurface(caster,su)
			;brush=CreateBrush(CurSurface)
			;BrushColor brush,0,0,0
			;BrushTexture brush,ShadowColorTex,0,0
			;BrushTexture brush,ShadowColorTex,0,1
			;BrushTexture brush,ShadowColorTex,0,2
			;BrushTexture brush,ShadowColorTex,0,3
			;BrushTexture brush,ShadowColorTex,0,4
			;BrushTexture brush,ShadowColorTex,0,5
			;BrushTexture brush,ShadowColorTex,0,6
			;BrushTexture brush,ShadowColorTex,0,7	
			;BrushFX brush,1+8
			;PaintSurface CurSurface,brush	
		;Next
	Else If Trans = 1
		;EntityFX caster,8
		;EntityAlpha Caster, 0
	End If
	
	HideEntity caster
	s.caster = New caster
	s\mesh = caster
	Return caster

End Function

;Set Shadow Caster
Function DisableCaster(mesh)	
	s.dcaster = New dcaster
	s\mesh = mesh
	Return mesh
End Function

;Set Shadow Receiver
Function SetReceiver(mesh)
	r.receiver = New receiver
	
	r\orig = mesh
	
	;get brush per surface	
	max_surfs = CountSurfaces(mesh)
	For s = 1 To max_surfs
		CurSurface = GetSurface(mesh,s)
		;get textures
		r\brushorig=GetSurfaceBrush(CurSurface)
		r\texure0 = GetBrushTexture(r\brushorig,0)
		r\texure1 = GetBrushTexture(r\brushorig,1)
		r\texure2 = GetBrushTexture(r\brushorig,2)
		r\texure3 = GetBrushTexture(r\brushorig,3)
		r\texure4 = GetBrushTexture(r\brushorig,4)
		r\texure5 = GetBrushTexture(r\brushorig,5)
		r\texure6 = GetBrushTexture(r\brushorig,6)
		r\texure7 = GetBrushTexture(r\brushorig,7)
	Next
	;r\mesh = CopyEntity(mesh)
	r\mesh = CreateMesh(mesh);<-slow
	EntityBlend 	r\mesh,2;;;;;;;;;;;;;;;;;;;;;;;;;
	PaintMesh r\mesh,Shadowbrush
	r\surf = CreateSurface(r\mesh);<-slow
	;max_surfs = CountSurfaces(mesh)
	;For s = 1 To max_surfs
	;	CurSurface = GetSurface(mesh,s)		
	;	r\surf = GetSurface(r\mesh,s)
		PaintSurface r\surf,ShadowBrush	
	;Next	
	

End Function


;Update Shadow System
Function UpdateShadowSystem(cam)

	;hide receivers
	Local r.receiver
	For r = Each receiver
		;HideEntity r\orig
		;Clear the receiver mesh, apply a clear texture to each surface and brush
		max_surfs = CountSurfaces(r\orig)
		For s = 1 To max_surfs
			CurSurface = GetSurface(r\orig,s)
			r\brush=CreateBrush(CurSurface)
			BrushTexture r\brush,ClearTex,0,0
			BrushTexture r\brush,ClearTex,0,1
			BrushTexture r\brush,ClearTex,0,2
			BrushTexture r\brush,ClearTex,0,3
			BrushTexture r\brush,ClearTex,0,4
			BrushTexture r\brush,ClearTex,0,5
			BrushTexture r\brush,ClearTex,0,6			
			BrushTexture r\brush,ClearTex,0,7
			BrushFX r\brush,1+2+8
			PaintSurface CurSurface,r\brush	
		Next
	Next
	;hide objects that dont cast shadows
	Local d.dcaster
	For d = Each dcaster
		HideEntity d\mesh
	Next

	CameraProjMode cam,0
	;<----------------
	;show casters
	;Local c.caster
	;For c = Each caster
	;	ShowEntity c\mesh
	;Next
	;update sustem
	

	If Not KeyDown(61) ProjectShadows()	

	;<----------------		
	;show receivers
	For r = Each receiver
		;ShowEntity r\orig
		;Recover the receiver mesh, apply all normal brushes and textures
		max_surfs = CountSurfaces(r\orig)
		For s = 1 To max_surfs
			CurSurface = GetSurface(r\orig,s)
			PaintSurface CurSurface,r\brushorig	
		Next
		;temporary
	Next
	;show objects that dont cast shadows
	For d = Each dcaster
		ShowEntity d\mesh
	Next

	If Not KeyDown(60) CameraProjMode cam,1
	
End Function

;free shadow system
Function FreeShadowSystem()
	Local c.caster
	For c = Each caster
		FreeEntity c\mesh
		Delete c
	Next
	
	For r.receiver = Each receiver
		FreeEntity r\mesh
		Delete r
	Next

	For dll.light = Each light
		Delete dll
	Next

	If ShadowTex	Then FreeTexture ShadowTex
	If ShadowBrush	Then FreeBrush ShadowBrush

	ShadowTex		= 0
	ShadowBrush	= 0

End Function
;internal function (projects shadow maps)
Function ProjectShadows()
	dll.light = First light
;For dll.light = Each light	
	If dll = Null Then Return

	; Hide Receiver meshes
	For dlr.receiver = Each receiver
		HideEntity dlr\mesh
	Next
	
	cleartexture(ShadowTex)	;<- I added this because alot of graphics cards seem to render fog differant to mine.. Hopefully this fixes the blurring problem.
			
	If dll\typ=2 Or dll\typ=3 Then
		CameraZoom dll\camera,1;ShadowScale# ;<--- This may need changing depending on the scene and light position
		CameraProjMode dll\camera,1 ;<---- Works with perspective as well, but you need to change the zoom
	Else dll\typ=1
		CameraZoom dll\camera,ShadowScale#/300 ;<--- This may need changing depending on the scene and light position
		CameraProjMode dll\camera,2 ;<---- Works with perspective as well, but you need to change the zoom
	End If	

	; Render shadow
	RenderWorld	
	Color 255,255,255: Rect 0,0,ShadowTexSize,ShadowTexSize,0 ;<-this rectangle is used to cull anything that tries to poke outside of the textures boundry box.
	CopyRect 0,0,ShadowTexSize,ShadowTexSize,0,0,BackBuffer(),TextureBuffer(ShadowTex)	
	CameraRange dll\camera, 1, dll\ShadowRange#	

	; Show Receiver meshes
	For dlr.receiver = Each receiver
		ShowEntity dlr\mesh
	Next

	;light caster props	
	x# = EntityX(dll\entity,True)
	y# = EntityY(dll\entity,True)
	z# = EntityZ(dll\entity,True)
	
	pitch# 	= EntityPitch(dll\entity,True)
	yaw# 	= EntityYaw(dll\entity,True)
	roll# 	= EntityRoll(dll\entity,True)

	TFormNormal 0, 0, 1, dll\entity, 0
	E_NX# = TFormedX#()
	E_NY# = TFormedY#()
	E_NZ# = TFormedZ#()
	
	For dlr.receiver = Each receiver
		If CountVertices(dlr\surf) = 0		
			;
			; No backface culling
			ClearSurface dlr\surf
			n_surfs = CountSurfaces(dlr\orig)
			;surface loop
			For s = 1 To n_surfs
				surf = GetSurface(dlr\orig,s)
				n_tris = CountTriangles(surf)-1
				;triangle loop
				For t = 0 To n_tris
					v0 = TriangleVertex(surf,t,0)
					v1 = TriangleVertex(surf,t,1)
					v2 = TriangleVertex(surf,t,2)

					nv0 = AddVertex(dlr\surf,VertexX(surf,v0),VertexY(surf,v0),VertexZ(surf,v0))
					nv1 = AddVertex(dlr\surf,VertexX(surf,v1),VertexY(surf,v1),VertexZ(surf,v1))
					nv2 = AddVertex(dlr\surf,VertexX(surf,v2),VertexY(surf,v2),VertexZ(surf,v2))

					AddTriangle dlr\surf,nv0,nv1,nv2
				Next
			Next			
		End If
			
		AmbMode=1
		AR=255
		AG=0
		AB=0
		AM=1
		
		;
		; Calculate uv's
		n_verts = CountVertices(dlr\surf)-1
		For v = 0 To n_verts
			TFormPoint VertexX(dlr\surf,v),VertexY(dlr\surf,v),VertexZ(dlr\surf,v),dlr\mesh,0
			CameraProject dll\camera,TFormedX(),TFormedY(),TFormedZ()
			tu# = ProjectedX()/Float(ShadowTexSize)
			tv# = ProjectedY()/Float(ShadowTexSize)
			VertexTexCoords dlr\surf,v,tu,tv			
			
			If AmbMode=1	
				;Ambient filter													
				;
				NX# = VertexNX#(dlr\surf, v)				
				NY# = VertexNY#(dlr\surf, v)	
				NZ# = VertexNZ#(dlr\surf, v)	
				;			
				TFormNormal NX#,NY#,NZ#,dlr\orig,dll\entity				

				Vred#=AR*AM
				Vgreen#=AG*AM
				Vblue#=AB*AM
				;
				VA# = (NX# * -E_NX#) + (NY# * -E_NY#) + (NZ# * -E_NZ#)	
				VC = -Va# * 65536.0
				If VC < 0 Then VC = 0
				VertexColor dlr\surf,v, (VC+(Vred)), (VC+(Vgreen)), (VC+(Vblue))
			Else
				VertexColor dlr\surf,v,AR,AG,AB
			End If
				
		Next


	Next

	; Update positions
	dll\lastx = x
	dll\lasty = y
	dll\lastz = z
	dll\lastpitch	= pitch
	dll\lastyaw		= yaw
	dll\lastroll	= roll

	; disable shadow camera
	CameraProjMode dll\camera,0
	
	;softshadows?
	If SoftShadow Then 
		BlurTexture(ShadowTex,SoftShadowQuality,SoftShadowRadius)
		TextureBlend ShadowTex, 3
		Color 255,255,255: Rect 0,0,ShadowTexSize,ShadowTexSize,0 ;<-this rectangle is used to cull anything that tries to poke outside of the textures boundry box.
		CopyRect 0,0,ShadowTexSize,ShadowTexSize,0,0,BackBuffer(),TextureBuffer(ShadowTex)
	EndIf


;Next

End Function

Function cleartexture(tex)
SetBuffer TextureBuffer( tex ) 
Color 255,255,255
Rect 0,0,TextureWidth(tex)+1,TextureHeight(tex)+1
SetBuffer BackBuffer()
End Function

;this function allow's soft shadow's
;This Function blurs a texture using 3D acceleration. It takes the original texture, creates a bunch of sprites, offsets them at various angles around a central point, adjusts the alpha of Each accoridngly, And Then renders the final result back To the specified texture.

;The Function works best with small blur radii, but with some modification could create several concentric rings of sprites To do larger radius blurs that look proper.

;Note that you won't get a proper gaussian blur out of this, it's a hack. But it's probably as good as you're going To get from a hardware acclerated blurring Function without pixel shaders.

; -------------------------------------------------------------------------------------------------------------------
; This function blurs a texture using a technique that takes advantage of 3D acceleration.  
;
; * You MUST hide all other cameras before calling this function!
; * You MUST reset your texture's blending mode, scale, and position after calling this function!
;
; Texture is the texture you want blurred.
;
; Blur_Quality defines the quality of the blur.  1 = 4 passes, 2 = 8 passes, 3 = 12 passes, etc.
;
;     (The reason that the passes are in multiples of four is because interference artifacts are created when
;     the number of passes is not a multiple of four... meaning that ten passes will actually look far worse
;     than eight.)
;
; Blur_Radius# defines the radius of the blur, in pixels, assuming a map size of 256x256.
;
;    (Ie, a radius of 16 will be the same width regardless of whether the texture is 16x16 or 512x512.  It will
;     only be exactly 16 pixels wide if the map is 256x256.)
; -------------------------------------------------------------------------------------------------------------------
Function BlurTexture(Texture, Blur_Quality, Blur_Radius#)

    ; This is used for temporary storage of the meshes used for soft shadow blurring.
    Local BlurMesh[16*4]
    Local Loop
    Local Blur_Cam

    Local BLUR_CAM_X# = 65536.0
    Local BLUR_CAM_Y# = 65536.0
    Local BLUR_CAM_Z# = 0.0

    ; If blurring is enabled...
    If Blur_Quality > 0

        Blur_Cam = CreateCamera()

        ; Set the camera viewport to the same size as the texture.        
        CameraViewport Blur_Cam, 0, 0, TextureWidth(Texture), TextureHeight(Texture)

        ; Set the camera so it clears the color buffer before rendering the texture.
        CameraClsColor Blur_Cam, 0, 0, 0
        CameraClsMode  Blur_Cam, True, True                        

        ; Set the camera's range to be very small so as to reduce the possiblity of extra objects making it into the scene.
        CameraRange Blur_Cam, 0.1, 100
    
        ; Set the camera to zoom in on the object to reduce perspective error from the object being too close to the camera.
      
		CameraZoom Blur_Cam,16.0

        ; Aim camera straight down.    
        RotateEntity Blur_Cam, 90, 0, 0, True
        
        ; Position the blur camera far from other entities in the world.
        PositionEntity Blur_Cam, BLUR_CAM_X#, BLUR_CAM_Y#, BLUR_CAM_Z#
        
        ; Create the sprites to use for blurring the shadow maps.
        For Loop = 0 To (Blur_Quality*4)-1
            BlurMesh[Loop] = CreateSprite()
        Next
        
        ; Set the texture blend mode to multiply.
        TextureBlend Texture, 2
                                                
        ; Scale the texture down because we scale the sprites up so they fill a larger area of the
        ; screen.  (Otherwise the edges of the texture are darker than the middle because they don't
        ; get covered.
        ScaleTexture    Texture, 0.5, 0.5
        PositionTexture Texture, 0.5, 0.5
                        
        ; Blur texture by blitting semi-transparent copies of it on top of it.
        BlurRadius# = Blur_Radius# * (1.0 / 256.0)
        BlurAngleStep# = 360.0 / Float(Blur_Quality*4)

        ; Normally we would just divide 255 by the number of passes so that adding all the passes
        ; together would not exceed 256.  However, if we did that, then we could not have a number of
        ; passes which does not divide 256 evenly, or else the error would result in the white part of
        ; the image being slightly less than white.  So we round partial values up to ensure that
        ; white will always be white, even if it ends up being a little whiter than white as a result
        ; when all the colors are added, since going higher than white just clamps to white.
        BlurShade = Ceil(255.0 / Float(Blur_Quality*4))
        
        ; Place each of the blur objects around a circle of radius blur_radius.
        For Loop = 0 To (Blur_Quality*4)-1
        
            EntityTexture BlurMesh[Loop], Texture
            EntityFX BlurMesh[Loop], 1+8
            EntityAlpha BlurMesh[Loop], 1.0 / Float(Loop+1)
            ScaleSprite BlurMesh[Loop], 4, 4; originally -> 2,2
                                                                                        
            BlurAngle# = BlurAngleStep# * Float(Loop) + 180.0*(Loop Mod 2)
                            
            Xoff# = BlurRadius# * Cos(BlurAngle#)
            Yoff# = BlurRadius# * Sin(BlurAngle#)

            PositionEntity BlurMesh[Loop], BLUR_CAM_X# + Xoff#, BLUR_CAM_Y# - 16, BLUR_CAM_Z# + Yoff#, True
                    
        Next
                    
        ; Render the new texture.
        RenderWorld
                
        ; Copy the new texture from the screen buffer to the texture buffer.        
        CopyRect 0, 0, TextureWidth(Texture), TextureHeight(Texture), 0, 0, BackBuffer(), TextureBuffer(Texture)
                    
        ; Free the blur entities.
        For Loop = 0 To (Blur_Quality*4)-1
            FreeEntity BlurMesh[Loop]
        Next
        ; Free the blur camera.
        FreeEntity Blur_Cam




    EndIf

End Function