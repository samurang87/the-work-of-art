import {useParams} from "react-router-dom";
import WorkOfArt from "../components/WorkOfArtComponent.tsx";

export default function WorkOfArtPage() {

    const {workOfArtId} = useParams<{ workOfArtId?: string }>();

    return (
        <div>
            <WorkOfArt workOfArtId={workOfArtId}/>
        </div>
    );
}
