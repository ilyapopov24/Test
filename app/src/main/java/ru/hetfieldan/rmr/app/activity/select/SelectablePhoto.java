package ru.hetfieldan.rmr.app.activity.select;



import org.jinstagram.entity.common.Images;

/**
 * Created by hetfieldan24 on 15.08.2014.
 */
public class SelectablePhoto
{
    private final String id;
    private final String full;
    private final String thumb;
    private boolean isSelected;

    public SelectablePhoto(Images images, String id)
    {
        this.id = id;
        thumb = images.getThumbnail().getImageUrl();
        full = images.getStandardResolution().getImageUrl();
    }
    public void invertSelection()
    {
        isSelected = !isSelected;
    }
    public String getThumb()
    {
        return thumb;
    }
    public boolean isSelected()
    {
        return isSelected;
    }
    public String getId()
    {
        return id;
    }
    public String getFull()
    {
        return full;
    }

}
