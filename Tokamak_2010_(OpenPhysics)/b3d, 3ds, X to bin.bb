Graphics3D 640,480,32,2

dir=ReadDir(CurrentDir())

Repeat
    file$=Lower$(NextFile$(Dir))

    If file$="" Then Exit

    If FileType(file$) = 1 Then
        r4$=Right$(file$,4)
        If r4$=".3ds" Or r4$=".b3d" Or Right$(file$,2)=".x" Then
            Print "Converting "+file$
            m=LoadMesh(file$)
            vcount=0
            For s=1 To CountSurfaces(m)
                surf=GetSurface(m,s)
                For i=0 To CountVertices(surf)-1
                    vcount=vcount+1
                Next
            Next
            If vcount>255 Then
                Print " *Can't process because there are more than 255 vertices"
            Else
                If r4$=".3ds" Or r4$=".b3d" Then file$=Left$(file$,Len(file$)-3) ElseIf Right$(file$,2)=".x" Then file$=Left$(file$,Len(file$)-1)
                fil=WriteFile(file$+"txt")
                WriteLine fil,vcount
                For s=1 To CountSurfaces(m)
                    surf=GetSurface(m,s)
                    For i=0 To CountVertices(surf)-1
                        WriteLine fil,VertexX(surf,i)+" "+VertexY(surf,i)+" "+VertexZ(surf,i)
                    Next
                Next
                CloseFile fil
                ExecFile "adjacency "+file$+"txt "+file$+"bin"
                Delay 100
                DeleteFile file$+"txt"
            EndIf
        EndIf
    End If
Forever

CloseDir Dir