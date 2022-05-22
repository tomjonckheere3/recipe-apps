<?php

namespace App\Notifications;

use App\Models\User;
use App\Modules\Notifications\Services\NotificationService;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Notifications\Messages\MailMessage;
use Illuminate\Notifications\Notification;
use NotificationChannels\WebPush\WebPushMessage;
use NotificationChannels\WebPush\WebPushChannel;
use Illuminate\Support\Facades\Log;
use App\Modules\Notifications\Services;

class FavouritedRecipe extends Notification
{
    use Queueable;

    public function __construct($body)
    {
        $this->body = $body;
    }

    public function via($notifiable){
        return [WebPushChannel::class];
    }

    public function toWebPush($notifiable, $notification){
        $message = (new WebPushMessage)
                        ->body($this->body);


        return $message;
    }
}
