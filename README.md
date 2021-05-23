Jerry Yu, 20764520  <br />
Java 11 <br />
Windows 10 <br />
Line Images were drawn personally <br />
Delete/Trash Icon: https://www.pinclipart.com/pindetail/iibwbhi_trash-svg-png-icon-free-download-delete-button/ <br />
Bezier Curve/Point Icon: https://thenounproject.com/term/bezier-curve/ <br />
Edit Button Icon: https://www.svgrepo.com/svg/42233/pencil-edit-button <br />
Selection Icon: https://thenounproject.com/term/mouse-pointer/ <br />


Bonus Feature: Advanced Drawing
Press select to start starting. Drag on the canvas and upon release the corresponding control point is set. Note that the first
drag and release does nothing as we cannot have a curve with any only point.

Click on a segment point and then toggle the control type by clicking on the point bottom. <br />
By default segment points are smooth which are indicated by a black circle. Sharp segment points are red. <br />
Sharp behaviour has been implemented with the second behaviour as indicated here: https://piazza.com/class/kjhbt0bo9qu8?cid=426
. As in, changing to sharp does not overlap the points but control points are independent.

Saving drawings are to be done to .txt files. This means when the save dialog opens you should save to a file called "example.txt".
Loading a textfile/drawing will place you in the edit mode. You can save by using the File -> Save dropdown. In addition, if you try to create
a new application or quit the application without saving, you will be prompted to save. 

You can edit curves and their properties by clicking the edit button and then selecting a curve. 
Clicking on a curve to edit it will make the control points turn green to indicate it has been selected.
This behaviour is accepted as alternate to bounding box. From here during your edit mode, you can change the
properties of the curve. Selecting a curve will also update the tools with the curve's properties. You can move the 
segment points and control points of a selected curve by dragging the points around in edit mode.

You can delete curves by clicking on Edit, then clicking a curve and then clicking the Delete button or Delete key on 
key board. You can also simply press ESC then press Delete and click on a curve.